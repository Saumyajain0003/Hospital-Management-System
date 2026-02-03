package com.medicare.service;

import com.medicare.exception.BadRequestException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimiterService {
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> createNewBucket());
    }
    
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
    
    public void checkRateLimit(String key) {
        Bucket bucket = resolveBucket(key);
        if (!bucket.tryConsume(1)) {
            log.warn("Rate limit exceeded for key: {}", key);
            throw new BadRequestException("Too many requests. Please try again later.");
        }
    }
}
