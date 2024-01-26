package com.trodix.duckcloud.connectors.onlyoffice.services;

import com.trodix.duckcloud.connectors.onlyoffice.models.UserRepresentation;

public interface OnlyOfficeUserService {

    String getUserId();
    String getName();
    UserRepresentation fetchUserProfile(String userId);

    String getDefaultUser();

    String getDefaultEmail();
}
