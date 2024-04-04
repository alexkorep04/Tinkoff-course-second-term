package edu.java.configuration;

import edu.java.configuration.retry.RetryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull
    String databaseAccessType,
    @NotNull
    boolean useQueue,
    @NotNull
    Retry retry,
    @NotNull
    Kafka kafka
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Retry(@NotNull RetryType retryType,
                        @NotNull @NotEmpty List<Integer> statuses,
                        @NotNull int attempts,
                        @NotNull long delay) {}

    public record Kafka(@NotNull String bootstrapServers,
                        @NotNull String topicName) {
    }

}
