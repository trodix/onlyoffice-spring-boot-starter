package com.trodix.onlyoffice.controllers;

import com.trodix.onlyoffice.dto.requests.OnlyOfficeCallbackRequest;
import com.trodix.onlyoffice.dto.responses.OnlyOfficeUpdatedDocumentResponse;
import com.trodix.onlyoffice.models.OfficeDocument;
import com.trodix.onlyoffice.services.OnlyOfficeDocumentService;
import com.trodix.onlyoffice.services.OnlyOfficeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/integration/onlyoffice")
public class OnlyOfficeIntegrationController {

    private final OnlyOfficeDocumentService onlyOfficeDocumentService;

    private final OnlyOfficeService onlyOfficeService;

    @Operation(summary = "Get the file content")
    @GetMapping("/document/{documentId}/contents")
    public ResponseEntity<ByteArrayResource> getDocumentContentByNodeId(@PathVariable final String documentId) {
        OfficeDocument officeDocument = onlyOfficeDocumentService.findById(documentId);

        if (officeDocument == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found for documentId " + documentId);
        }

        final byte[] data;

        try {
            data = onlyOfficeDocumentService.getDocumentContent(documentId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filecontent not found for documentId " + documentId);
        }

        final ByteArrayResource resource = new ByteArrayResource(data);

        final String filename = officeDocument.getFileName();

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @Operation(summary = "Update the file content", description = "See https://api.onlyoffice.com/editors/callback")
    @PostMapping(path = "/document", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public OnlyOfficeUpdatedDocumentResponse updateDocumentContent(@Valid @RequestBody final OnlyOfficeCallbackRequest data, HttpServletRequest req) {

        log.debug("Document data received from OnlyOffice: \n" + data);

        switch (data.getStatus()) {
            case READY_FOR_SAVING:
            case SAVING_ERROR:
            case DOCUMENT_EDITED_STATE_SAVED:
            case FORCE_SAVING_ERROR:
                if (data.getUrl() == null) {
                    log.trace("Received event from OnlyOffice without document url");
                    break;
                }
                String[] keyParts = data.getKey().split("_");
                String nodeId = keyParts[0];
                String revision = keyParts[1];
                onlyOfficeService.updateDocument(nodeId, revision, data.getUrl());
                break;
            default:
                break;
        }

        return new OnlyOfficeUpdatedDocumentResponse(0);
    }

    @Operation(summary = "Generate a OnlyOffice config containing a JWT for the user in order to granting access to OnlyOffice Document Server",
            description = """
                See :
                    https://api.onlyoffice.com/editors/signature/#java
                    https://api.onlyoffice.com/editors/signature/browser
            """
    )
    @PostMapping(path = "/open-document-request/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenOnlyOfficeDocumentRequestConfig(@PathVariable final String documentId) {
        return onlyOfficeService.generateEditorConfig(documentId).toString();
    }

}
