package com.nanditha.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Analytics data for a shortened URL")
public class AnalyticsResponse {

    @Schema(description = "Unique short code", example = "aB3xK9")
    private String shortCode;

    @Schema(description = "Original long URL", example = "https://google.com")
    private String originalUrl;

    @Schema(description = "Total number of redirects", example = "12")
    private Long clickCount;

    @Schema(description = "When the short URL was created", example = "2026-06-19T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "When the short URL expires", example = "2026-07-19T10:30:00")
    private LocalDateTime expiryDate;
}
