package edu.java.bot.configuration;

import edu.java.bot.configuration.retry.RetryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull
    Retry retry,

    @NotNull
    Kafka kafka
) {

    public record Retry(@NotNull RetryType retryType,
                        @NotNull @NotEmpty List<Integer> statuses,
                        @NotNull int attempts,
                        @NotNull long delay) {}

    public record Kafka(@NotNull String bootstrapServers,
                        @NotNull String topicName,
                        @NotNull Consumer consumer,
                        @NotNull String errorTopicName) {

        public record Consumer(@NotNull String groupId) {
        }
    }
}
