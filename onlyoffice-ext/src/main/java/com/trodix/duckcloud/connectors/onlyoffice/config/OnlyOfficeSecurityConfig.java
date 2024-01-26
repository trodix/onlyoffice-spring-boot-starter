package com.trodix.duckcloud.connectors.onlyoffice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class OnlyOfficeSecurityConfig {

    private final OnlyOfficeJwtAuthProvider onlyOfficeJwtAuthProvider;

    @Bean
    public AuthenticationManager onlyOfficeAuthManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(onlyOfficeJwtAuthProvider);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain onlyOfficeFilter(final HttpSecurity http, final AuthenticationManager onlyOfficeAuthManager) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/integration/onlyoffice/**")
                .authenticated()
                .and()
                .authenticationManager(onlyOfficeAuthManager);

        return http.build();
    }

}
