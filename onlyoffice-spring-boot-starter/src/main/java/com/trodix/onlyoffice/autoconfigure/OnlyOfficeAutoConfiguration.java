package com.trodix.onlyoffice.autoconfigure;

import com.trodix.onlyoffice.properties.OnlyOfficeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ComponentScan("com.trodix.onlyoffice")
@EnableConfigurationProperties({OnlyOfficeProperties.class})
public class OnlyOfficeAutoConfiguration {

    private final OnlyOfficeProperties properties;

    public OnlyOfficeAutoConfiguration(OnlyOfficeProperties properties) {
        this.properties = properties;
    }

    @Bean
    public SecurityFilterChain filterChainOnlyOffice(final HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();

        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/api/vi/integration/onlyoffice/**")
                    .authenticated();
        });

        return http.build();
    }

}
