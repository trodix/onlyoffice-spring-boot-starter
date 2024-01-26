package com.trodix.onlyoffice.dto.requests;

import java.util.List;

public enum OnlyOfficeDocumentType {

    WORD,
    CELL,
    SLIDE;

    public static List<String> getSupportByWordExtensions() {
        return List.of(
            "doc", "docm", "docx", "docxf", "dot", "dotm", "dotx",
            "epub", "fodt", "fb2", "htm", "html", "mht", "odt", "oform", "ott", "oxps",
            "pdf", "rtf", "txt", "djvu", "xml", "xps"
        );
    }

    public static List<String> getSupportByCellExtensions() {
        return List.of("csv", "fods", "ods", "ots", "xls", "xlsb", "xlsm", "xlsx", "xlt", "xltm", "xltx");
    }

    public static List<String> getSupportBySlideExtensions() {
        return List.of("fodp", "odp", "otp", "pot", "potm", "potx", "pps", "ppsm", "ppsx", "ppt", "pptm", "pptx");
    }

}
