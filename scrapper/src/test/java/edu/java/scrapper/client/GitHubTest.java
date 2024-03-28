package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.client.DefaultGitHubClient;
import edu.java.client.GitHubClient;
import edu.java.entity.GitHubResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Predicate;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GitHubTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    private GitHubClient gitHubClient;

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
        Predicate<Throwable> filter = doFilter(List.of(500));
        return Retry.fixedDelay(1,
                Duration.ofMillis(1000))
            .filter(filter);
    }

    @Before
    public void init() {
        String baseUrl = wireMockRule.baseUrl();
        Retry retry = getConstantRetry();
        gitHubClient = new DefaultGitHubClient(retry, baseUrl);
    }

    @Test
    @DisplayName("Test response from GitHub")
    public void testGitResponse() {
        stubFor(get(urlEqualTo("/repos/alexkorep04/TestingWithWire"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{" +
                    "\"id\": \"12345\", " +
                    "\"name\": \"TestingWithWire\", " +
                    "\"html_url\": \"https://github.com/alexkorep04/TestingWithWire\", " +
                    "\"description\": \"Test program\", " +
                    "\"created_at\": \"2021-01-01T00:00:00Z\", " +
                    "\"updated_at\": \"2022-01-01T00:00:00Z\", " +
                    "\"pushed_at\": \"2022-01-01T00:00:00Z\", " +
                    "\"open_issues_count\": \"1\" " +
                    "}")));
        Mono<GitHubResponse> responseMono = gitHubClient.fetchRepository("alexkorep04", "TestingWithWire");
        GitHubResponse response = responseMono.block();

        assertThat(response).isNotNull();
        assertThat(12345).isEqualTo(response.getId());
        assertThat("TestingWithWire").isEqualTo(response.getName());
        assertThat("https://github.com/alexkorep04/TestingWithWire").isEqualTo(response.getHtmlUrl());
        assertThat("Test program").isEqualTo(response.getDescription());
        assertThat(OffsetDateTime.parse("2021-01-01T00:00:00Z")).isEqualTo(response.getCreateTime());
        assertThat(OffsetDateTime.parse("2022-01-01T00:00:00Z")).isEqualTo(response.getUpdateTime());
        assertThat(OffsetDateTime.parse("2022-01-01T00:00:00Z")).isEqualTo(response.getPushedTime());
        assertThat(1).isEqualTo(response.getIssuesCount());
    }
}
