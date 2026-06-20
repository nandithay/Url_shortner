package com.nanditha.urlshortener.controller;

import com.nanditha.urlshortener.dto.ErrorResponse;
import com.nanditha.urlshortener.dto.ShortenUrlRequest;
import com.nanditha.urlshortener.dto.ShortenUrlResponse;
import com.nanditha.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/urls")
@Tag(name = "URL Shortening", description = "Create shortened URLs")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    @Operation(summary = "Create a short URL", description = "Generates a unique short code for the given original URL and stores it in MySQL and Redis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Short URL created successfully",
                    content = @Content(schema = @Schema(implementation = ShortenUrlResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ShortenUrlResponse createShortUrl(@RequestBody ShortenUrlRequest request) {
        return urlService.createShortUrl(request);
    }
}
