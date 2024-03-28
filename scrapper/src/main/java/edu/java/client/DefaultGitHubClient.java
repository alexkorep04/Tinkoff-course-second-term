package edu.java.client;

import edu.java.entity.BaseURL;
import edu.java.entity.GitHubResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class DefaultGitHubClient implements GitHubClient {
    private final WebClient webClient;
    private final Retry retry;

    public DefaultGitHubClient(Retry retry, String baseURL) {
        this.webClient = WebClient.create(baseURL);
        this.retry = retry;
    }

    public DefaultGitHubClient(Retry retry) {
        this(retry, BaseURL.GITHUB.getUrl());
    }

    @Override
    public Mono<GitHubResponse> fetchRepository(String owner, String name) {
        return webClient.get()
            .uri("/repos/" + owner + "/" + name)
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(GitHubResponse.class)
            .retryWhen(retry);
    }


}
