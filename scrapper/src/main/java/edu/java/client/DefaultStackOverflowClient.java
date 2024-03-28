package edu.java.client;

import edu.java.entity.BaseURL;
import edu.java.entity.StackOverflowResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class DefaultStackOverflowClient implements StackOverflowClient {

    private final WebClient webClient;
    private final Retry retry;

    public DefaultStackOverflowClient(Retry retry, String baseURL) {
        this.webClient = WebClient.create(baseURL);
        this.retry = retry;
    }

    public DefaultStackOverflowClient(Retry retry) {
        this(retry, BaseURL.STACKOVERFLOW.getUrl());
    }

    @Override
    public Mono<StackOverflowResponse> fetchQuestion(int id) {
        return webClient
            .get()
            .uri("/questions/" + id + "?site=stackoverflow")
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(StackOverflowResponse.class)
            .retryWhen(retry);
    }
}
