package com.trodix.duckcloud.connectors.onlyoffice.services;

import com.trodix.duckcloud.connectors.onlyoffice.models.OfficeDocument;

public interface DocumentService {

    OfficeDocument findById(String documentId);

    void updateContent(OfficeDocument document, byte[] content);

    byte[] getDocumentContent(String documentId);
}
