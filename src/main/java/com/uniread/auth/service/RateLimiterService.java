package com.uniread.auth.service;

import com.uniread.auth.core.RateLimited;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimiterService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key, RateLimited rateLimited) {
        return buckets.computeIfAbsent(key, k -> createNewBucket(rateLimited));
    }

    private Bucket createNewBucket(RateLimited rateLimited) {
        Refill refill = Refill.intervally(rateLimited.refillTokens(), parseDuration(rateLimited));
        Bandwidth limit = Bandwidth.classic(rateLimited.capacity(), refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private Duration parseDuration(RateLimited rateLimited) {
        return switch(rateLimited.refillDurationType()) {
            case MINUTE -> Duration.ofMinutes(rateLimited.refillDuration());
            case HOUR -> Duration.ofHours(rateLimited.refillDuration());
            case DAY -> Duration.ofDays(rateLimited.refillDuration());
        };
    }
}
