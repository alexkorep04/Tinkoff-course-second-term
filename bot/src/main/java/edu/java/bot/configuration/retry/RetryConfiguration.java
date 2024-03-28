package edu.java.bot.configuration.retry;

import edu.java.bot.configuration.ApplicationConfig;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig applicationConfig;

    private Predicate<Throwable> doFilter(List<Integer> codes) {
        return exception -> {
            if (exception instanceof HttpClientErrorException) {
                int statusCodeValue = ((HttpClientErrorException) exception).getStatusCode().value();
                return codes.contains(statusCodeValue);
            } else {
                return false;
            }
        };
    }

    private Retry getConstantRetry() {
        Predicate<Throwable> filter = doFilter(applicationConfig.retry().statuses());
        return Retry.fixedDelay(applicationConfig.retry().attempts(),
                Duration.ofMillis(applicationConfig.retry().delay()))
            .filter(filter);
    }

    private Retry getExponentialRetry() {
        Predicate<Throwable> filter = doFilter(applicationConfig.retry().statuses());
        return Retry.backoff(applicationConfig.retry().attempts(),
            Duration.ofMillis(applicationConfig.retry().delay()))
            .filter(filter);
    }

    private Retry getLinearRetry() {
        Predicate<Throwable> filter = doFilter(applicationConfig.retry().statuses());
        Duration delay = Duration.ofMillis(applicationConfig.retry().delay());
        return Retry.from(signal -> Flux.deferContextual(
            cv -> signal.contextWrite(cv)
                    .concatMap(retryWhenState -> {
                        Retry.RetrySignal copy = retryWhenState.copy();
                        Throwable currentFailure = copy.failure();
                        long iteration = copy.totalRetries();
                        if (currentFailure == null) {
                            return Mono.error(
                                new IllegalStateException("Retry.RetrySignal#failure() not expected to be null")
                            );
                        }
                        if (!filter.test(currentFailure)) {
                            return Mono.error(currentFailure);
                        }
                        if (iteration >= applicationConfig.retry().attempts()) {
                            return Mono.error(new ExhaustedRetryException("Retry failed on iteration " + iteration));
                        }
                        Duration nextBackoff = delay.multipliedBy(2 * iteration);
                        return Mono.delay(nextBackoff, Schedulers.parallel());
                    })
            )
        );
    }

    @Bean
    public Retry getRetry() {
        RetryType retryType = applicationConfig.retry().retryType();
        if (RetryType.CONSTANT.equals(retryType)) {
            return getConstantRetry();
        } else if (RetryType.EXPONENTIAL.equals(retryType)) {
            return  getExponentialRetry();
        } else if (RetryType.LINEAR.equals(retryType)) {
            return getLinearRetry();
        }
        return null;
    }
}
