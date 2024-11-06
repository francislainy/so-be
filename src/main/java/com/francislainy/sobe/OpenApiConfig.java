package com.francislainy.sobe;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPIV3Parser()
            .read("src/main/resources/openapi.yml");
        if (openAPI == null) {
            throw new IllegalStateException("Failed to parse OpenAPI file.");
        }
        return openAPI;
    }
}
