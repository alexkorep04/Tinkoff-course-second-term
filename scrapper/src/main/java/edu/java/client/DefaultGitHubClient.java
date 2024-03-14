package edu.java.client;

import edu.java.entity.BaseURL;
import edu.java.entity.GitHubResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public class DefaultGitHubClient implements GitHubClient {
    private final WebClient webClient;

    public DefaultGitHubClient(String baseURL) {
        this.webClient = WebClient.create(baseURL);
    }

    public DefaultGitHubClient() {
        this(BaseURL.GITHUB.getUrl());
    }

    @Override
    public Mono<GitHubResponse> fetchRepository(String owner, String name) {
        return webClient.get().uri("/repos/"
            + owner + "/" + name).retrieve().bodyToMono(GitHubResponse.class);
    }
}
