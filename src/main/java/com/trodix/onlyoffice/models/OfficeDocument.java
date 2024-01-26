package com.trodix.onlyoffice.models;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OfficeDocument {

    private String id;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
    private String fileName;

}
