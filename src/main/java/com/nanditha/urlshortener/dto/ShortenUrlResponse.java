package com.nanditha.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response after creating a short URL")
public class ShortenUrlResponse {

    @Schema(description = "Generated short code", example = "aB3xK9")
    private String shortCode;

    @Schema(description = "Full shortened URL", example = "http://localhost:8080/aB3xK9")
    private String shortUrl;
}
