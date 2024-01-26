package com.trodix.duckcloud.connectors.onlyoffice.dto.requests;

import lombok.Data;

@Data
public class OnlyOfficeAction {

    private OnlyOfficeActionType type;

    private String userid;

}