package com.trodix.duckcloud.connectors.onlyoffice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trodix.duckcloud.connectors.onlyoffice.dto.requests.OnlyOfficeDocumentType;
import com.trodix.duckcloud.connectors.onlyoffice.models.OfficeDocument;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OnlyOfficeService {

    private final DocumentService documentService;

    private final OnlyOfficeUserService onlyOfficeUserService;

    @Value("${onlyoffice.jwt.secret}")
    private String onlyOfficeJwtSecret;

    @Value("${onlyoffice.appserver.public-base-url}")
    private String serverPublicBaseUrl;

    public String createJwtToken(JsonObject payloadClaims) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> payload = mapper.readValue(payloadClaims.toString(), new TypeReference<>() {
            });

            Signer signer = HMACSigner.newSHA256Signer(onlyOfficeJwtSecret);
            JWT jwt = new JWT();
            for (Map.Entry<String, Object> entry : payload.entrySet()) {
                jwt.addClaim(entry.getKey(), entry.getValue());
            }
            return JWT.getEncoder().encode(jwt, signer);
        } catch (Exception e) {
            return "";
        }
    }

    public JsonObject generateEditorConfig(String documentId) {

        OfficeDocument officeDocument = documentService.findById(documentId);

        String filename = officeDocument.getFileName();
        String fileExt = filename.substring(filename.lastIndexOf('.') + 1);
        String key = generateOnlyOfficeDocumentKey(officeDocument);
        String documentType = getOnlyOfficeDocumentType(fileExt).toString().toLowerCase();
        String downloadUrl = serverPublicBaseUrl + "/api/v1/integration/onlyoffice/document/" + officeDocument.getId() + "/contents";
        String callbackUrl = serverPublicBaseUrl + "/api/v1/integration/onlyoffice/document";
        String lang = System.getProperty("user.language") + "_" + System.getProperty("user.country");

        JsonObject config = Json.createObjectBuilder()
                .add("document", Json.createObjectBuilder()
                        .add("title", filename)
                        .add("url", downloadUrl)
                        .add("fileType", fileExt)
                        .add("key", key)
                        .add("permissions", Json.createObjectBuilder()
                                .add("edit", true)
                                .add("download", true)
                                .add("fillForms", true)
                                .add("copy", true)
                                .add("comment", true)))
                .add("documentType", documentType)
                .add("editorConfig", Json.createObjectBuilder()
                        .add("callbackUrl", callbackUrl)
                        .add("lang", lang)
                        .add("user", Json.createObjectBuilder()
                                .add("id", onlyOfficeUserService.getUserId())
                                .add("name", onlyOfficeUserService.getName())))
                .build();

        String token = createJwtToken(config);
        config = Json.createObjectBuilder(config)
                .add("token", token)
                .build();

        return config;
    }

    public void updateDocument(String documentId, String revision, String url) {
        log.debug("Updating document (nodeId={} revision={}) from url: {}", documentId, revision, url);

        OfficeDocument officeDocument = documentService.findById(documentId);

        try (InputStream is = new URL(url).openConnection().getInputStream()) {
            documentService.updateContent(officeDocument, is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateOnlyOfficeDocumentKey(OfficeDocument officeDocument) {
        OffsetDateTime createdAt = officeDocument.getCreatedAt();
        OffsetDateTime modifiedAt = officeDocument.getModifiedAt();
        OffsetDateTime lastModified = modifiedAt == null ? createdAt : modifiedAt;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String lastModifiedFormatted = df.format(lastModified);
        return String.format("%s_%s", officeDocument.getId(), lastModifiedFormatted);
    }

    private OnlyOfficeDocumentType getOnlyOfficeDocumentType(String extension) {

        if (OnlyOfficeDocumentType.getSupportByWordExtensions().contains(extension)) {
            return OnlyOfficeDocumentType.WORD;
        } else if (OnlyOfficeDocumentType.getSupportByCellExtensions().contains(extension)) {
            return OnlyOfficeDocumentType.CELL;
        } else if (OnlyOfficeDocumentType.getSupportBySlideExtensions().contains(extension)) {
            return OnlyOfficeDocumentType.SLIDE;
        }

        throw new IllegalArgumentException("file extension " + extension + " not supported");
    }
}
