package edu.java.client;

import edu.java.entity.BaseURL;
import edu.java.entity.StackOverflowResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public class DefaultStackOverflowClient implements StackOverflowClient {

    private final WebClient webClient;

    public DefaultStackOverflowClient(String baseURL) {
        this.webClient = WebClient.create(baseURL);
    }

    public DefaultStackOverflowClient() {
        this(BaseURL.STACKOVERFLOW.getUrl());
    }

    @Override
    public Mono<StackOverflowResponse> fetchQuestion(int id) {
        return webClient.get().uri("/questions/" + id
            + "?site=stackoverflow").retrieve().bodyToMono(StackOverflowResponse.class);
    }
}
