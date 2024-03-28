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
    Retry retry
) {

    public record Retry(@NotNull RetryType retryType,
                        @NotNull @NotEmpty List<Integer> statuses,
                        @NotNull int attempts,
                        @NotNull long delay) {}
}
