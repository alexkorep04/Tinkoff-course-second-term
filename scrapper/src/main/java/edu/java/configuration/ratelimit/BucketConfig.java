package edu.java.configuration.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketConfig {
    private final static int AMOUNT = 5;
    private final static int SECONDS = 30;

    @Bean
    public Bucket getBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(AMOUNT, Refill.intervally(AMOUNT, Duration.ofSeconds(SECONDS))))
            .build();
    }
}
