package com.trodix.onlyoffice.services;

import com.trodix.onlyoffice.models.OfficeDocument;

public interface OnlyOfficeDocumentService {

    OfficeDocument findById(String documentId);

    void updateContent(OfficeDocument document, byte[] content);

    byte[] getDocumentContent(String documentId);
}
