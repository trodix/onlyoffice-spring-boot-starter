package com.trodix.onlyoffice.dto.requests;

public enum OnlyOfficeForceSaveType {

    SAVED_BY_COMMAND_SERVICE(0),
    SAVED_BY_USER(1),
    AUTO_SAVE(2);

    private final int actionValue;

    OnlyOfficeForceSaveType(int actionValue) {
        this.actionValue = actionValue;
    }

    public int getActionValue() {
        return this.actionValue;
    }

    public OnlyOfficeForceSaveType getOnlyForceSaveType(int actionValue) {
        for (OnlyOfficeForceSaveType action : values()) {
            if (action.getActionValue() == actionValue) {
                return action;
            }
        }

        return null;
    }

}