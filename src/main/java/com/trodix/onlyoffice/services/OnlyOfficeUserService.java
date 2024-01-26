package com.trodix.onlyoffice.services;

import com.trodix.onlyoffice.models.UserRepresentation;

public interface OnlyOfficeUserService {

    String getUserId();
    String getName();
    UserRepresentation fetchUserProfile(String userId);

    String getDefaultUser();

    String getDefaultEmail();
}
