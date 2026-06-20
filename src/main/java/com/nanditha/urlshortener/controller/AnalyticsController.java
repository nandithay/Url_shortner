package com.nanditha.urlshortener.controller;

import com.nanditha.urlshortener.dto.AnalyticsResponse;
import com.nanditha.urlshortener.dto.ErrorResponse;
import com.nanditha.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "View click statistics for shortened URLs")
public class AnalyticsController {

    private final UrlService urlService;

    public AnalyticsController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Get URL analytics", description = "Returns click count and metadata for a shortened URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AnalyticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Short URL not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public AnalyticsResponse getAnalytics(
            @Parameter(description = "6-character Base62 short code", example = "aB3xK9")
            @PathVariable String shortCode) {
        return urlService.getAnalytics(shortCode);
    }
}
