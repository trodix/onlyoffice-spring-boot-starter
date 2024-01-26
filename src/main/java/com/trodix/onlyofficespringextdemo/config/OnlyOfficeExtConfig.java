package com.trodix.onlyofficespringextdemo.config;

import com.trodix.onlyoffice.models.OfficeDocument;
import com.trodix.onlyoffice.models.UserRepresentation;
import com.trodix.onlyoffice.services.OnlyOfficeDocumentService;
import com.trodix.onlyoffice.services.OnlyOfficeUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OnlyOfficeExtConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();

        // OAUTH authentication
        http
                .authorizeHttpRequests()
                .requestMatchers("/actuator/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        ;

        return http.build();
    }

    @Bean
    public OnlyOfficeUserService onlyOfficeUserService() {
        return new OnlyOfficeUserService() {
            @Override
            public String getUserId() {
                return "userId";
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public UserRepresentation fetchUserProfile(String userId) {
                return new UserRepresentation();
            }

            @Override
            public String getDefaultUser() {
                return "defaultUser";
            }

            @Override
            public String getDefaultEmail() {
                return "defaultEmail";
            }
        };
    }

    @Bean
    public OnlyOfficeDocumentService documentService() {
        return new OnlyOfficeDocumentService() {
            @Override
            public OfficeDocument findById(String documentId) {
                return new OfficeDocument();
            }

            @Override
            public void updateContent(OfficeDocument document, byte[] content) {

            }

            @Override
            public byte[] getDocumentContent(String documentId) {
                return new byte[0];
            }
        };
    }

}
