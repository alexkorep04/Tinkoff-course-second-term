package edu.java.configuration.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CounterConfig {

    @Bean
    public Counter getCounter() {
        return Metrics.counter("messages_counter");
    }
}
