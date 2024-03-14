package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.client.DefaultGitHubClient;
import edu.java.client.GitHubClient;
import edu.java.entity.GitHubResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GitHubTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    private GitHubClient gitHubClient;

    @Before
    public void init() {
        String baseUrl = wireMockRule.baseUrl();
        gitHubClient = new DefaultGitHubClient(baseUrl);
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
                    "\"updated_at\": \"2022-01-01T00:00:00Z\"" +
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
    }

}
