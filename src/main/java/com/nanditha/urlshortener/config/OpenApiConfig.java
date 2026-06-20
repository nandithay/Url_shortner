package com.nanditha.urlshortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI urlShortenerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("REST API for shortening URLs, redirecting, and viewing analytics.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nanditha")
                                .email("nanditha@example.com")));
    }
}
