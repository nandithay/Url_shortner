package com.nanditha.urlshortener.service;

import com.nanditha.urlshortener.dto.AnalyticsResponse;
import com.nanditha.urlshortener.dto.ShortenUrlRequest;
import com.nanditha.urlshortener.dto.ShortenUrlResponse;
import com.nanditha.urlshortener.entity.UrlMapping;
import com.nanditha.urlshortener.exception.UrlExpiredException;
import com.nanditha.urlshortener.exception.UrlNotFoundException;
import com.nanditha.urlshortener.repository.UrlRepository;
import com.nanditha.urlshortener.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final String baseUrl;

    public UrlService(
            UrlRepository urlRepository,
            RedisTemplate<String, String> redisTemplate,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
        this.baseUrl = baseUrl;
    }

    public ShortenUrlResponse createShortUrl(ShortenUrlRequest request) {
        if (!StringUtils.hasText(request.getOriginalUrl())) {
            throw new IllegalArgumentException("originalUrl must not be blank");
        }

        String shortCode = generateUniqueShortCode();
        LocalDateTime now = LocalDateTime.now();
        String originalUrl = request.getOriginalUrl().trim();

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortCode(shortCode);
        urlMapping.setClickCount(0L);
        urlMapping.setCreatedAt(now);
        urlMapping.setExpiryDate(now.plusDays(request.getExpiryDays()));

        urlRepository.save(urlMapping);

        redisTemplate.opsForValue().set(
                shortCode,
                originalUrl,
                Duration.ofDays(request.getExpiryDays()));

        ShortenUrlResponse response = new ShortenUrlResponse();
        response.setShortCode(shortCode);
        response.setShortUrl(buildShortUrl(shortCode));
        return response;
    }

    @Transactional
    public String resolveOriginalUrl(String shortCode) {
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) {
            recordClick(shortCode);
            return cachedUrl;
        }

        UrlMapping urlMapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        if (isExpired(urlMapping.getExpiryDate())) {
            throw new UrlExpiredException(shortCode);
        }

        recordClick(shortCode);

        String originalUrl = urlMapping.getOriginalUrl();
        cacheInRedis(shortCode, originalUrl, urlMapping.getExpiryDate());
        return originalUrl;
    }

    public AnalyticsResponse getAnalytics(String shortCode) {
        UrlMapping urlMapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        AnalyticsResponse response = new AnalyticsResponse();
        response.setShortCode(urlMapping.getShortCode());
        response.setOriginalUrl(urlMapping.getOriginalUrl());
        response.setClickCount(urlMapping.getClickCount());
        response.setCreatedAt(urlMapping.getCreatedAt());
        response.setExpiryDate(urlMapping.getExpiryDate());
        return response;
    }

    private void recordClick(String shortCode) {
        int updatedRows = urlRepository.incrementClickCount(shortCode);
        if (updatedRows == 0) {
            throw new UrlNotFoundException(shortCode);
        }
    }

    private boolean isExpired(LocalDateTime expiryDate) {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    private void cacheInRedis(String shortCode, String originalUrl, LocalDateTime expiryDate) {
        if (expiryDate == null) {
            redisTemplate.opsForValue().set(shortCode, originalUrl);
            return;
        }

        Duration ttl = Duration.between(LocalDateTime.now(), expiryDate);
        if (!ttl.isPositive()) {
            throw new UrlExpiredException(shortCode);
        }

        redisTemplate.opsForValue().set(shortCode, originalUrl, ttl);
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = ShortCodeGenerator.generate();
        } while (urlRepository.existsByShortCode(shortCode));
        return shortCode;
    }

    private String buildShortUrl(String shortCode) {
        String normalizedBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return normalizedBase + "/" + shortCode;
    }
}
