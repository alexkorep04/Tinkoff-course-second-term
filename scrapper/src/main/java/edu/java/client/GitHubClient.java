package edu.java.client;

import edu.java.entity.GitHubResponse;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<GitHubResponse> fetchRepository(String owner, String name);
}
