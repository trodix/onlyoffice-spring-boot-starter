package com.trodix.duckcloud.connectors.onlyoffice.dto.requests;

public enum OnlyOfficeDocumentStatus {

    DOCUMENT_EDITED(1),
    READY_FOR_SAVING(2),
    SAVING_ERROR(3),
    CLOSE_WITH_NO_CHANGED(4),
    DOCUMENT_EDITED_STATE_SAVED(6),
    FORCE_SAVING_ERROR(7)
    ;

    private final int actionValue;

    OnlyOfficeDocumentStatus(int actionValue) {
        this.actionValue = actionValue;
    }

    public int getActionValue() {
        return this.actionValue;
    }

    public OnlyOfficeDocumentStatus getOnlyDocumentStatus(int actionValue) {
        for (OnlyOfficeDocumentStatus action : values()) {
            if (action.getActionValue() == actionValue) {
                return action;
            }
        }

        return null;
    }

}