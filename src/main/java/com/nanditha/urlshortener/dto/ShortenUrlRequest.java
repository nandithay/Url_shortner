package com.nanditha.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a short URL")
public class ShortenUrlRequest {

    @Schema(description = "The original URL to shorten", example = "https://google.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String originalUrl;

    @Schema(description = "Number of days until the short URL expires", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer expiryDays;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Integer getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
    }
}
