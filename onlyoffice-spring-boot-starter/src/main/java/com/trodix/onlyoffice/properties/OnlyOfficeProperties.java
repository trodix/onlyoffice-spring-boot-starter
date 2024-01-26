package com.trodix.onlyoffice.properties;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "onlyoffice")
public class OnlyOfficeProperties {

    @NotEmpty
    private String jwtSecret;

    @NotEmpty
    @URL
    private String appServerBaseUrl;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String getAppServerBaseUrl() {
        return appServerBaseUrl;
    }

    public void setAppServerBaseUrl(String appServerBaseUrl) {
        this.appServerBaseUrl = appServerBaseUrl;
    }
}
