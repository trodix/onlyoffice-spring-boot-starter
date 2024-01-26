package com.trodix.onlyoffice.dto.requests;

import lombok.Data;

@Data
public class OnlyOfficeAction {

    private OnlyOfficeActionType type;

    private String userid;

}