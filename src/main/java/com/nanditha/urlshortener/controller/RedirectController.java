package com.nanditha.urlshortener.controller;

import com.nanditha.urlshortener.dto.ErrorResponse;
import com.nanditha.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@Controller
@Tag(name = "Redirect", description = "Redirect to the original URL using a short code")
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortCode:[a-zA-Z0-9]{6}}")
    @Operation(summary = "Redirect to original URL", description = "Resolves the short code from Redis or MySQL and redirects with HTTP 302.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect to the original URL",
                    headers = @Header(name = "Location", description = "The original URL", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Short URL not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "410", description = "Short URL has expired",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> redirect(
            @Parameter(description = "6-character Base62 short code", example = "aB3xK9")
            @PathVariable String shortCode) {
        String originalUrl = urlService.resolveOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
