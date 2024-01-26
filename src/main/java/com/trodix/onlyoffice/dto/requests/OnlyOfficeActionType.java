package com.trodix.onlyoffice.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OnlyOfficeActionType {

    USER_DISCONECTED(0),
    USER_CONNECTED(1),
    FORCE_SAVE(2);

    private final int actionValue;

    OnlyOfficeActionType(int actionValue) {
        this.actionValue = actionValue;
    }

    public int getActionValue() {
        return this.actionValue;
    }

    public OnlyOfficeActionType getOnlyOfficeAction(int actionValue) {
        for (OnlyOfficeActionType action : values()) {
            if (action.getActionValue() == actionValue) {
                return action;
            }
        }

        return null;
    }

}