package edu.java.client;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.entity.BaseURL;
import edu.java.exception.UpdateAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final WebClient webClient;
    private static final String UPDATE_EXCEPTION = "edu.java.exception.UpdateAlreadyExistsException";
    private static final String NOT_VALID_DATA = "Not valid data!";
    private static final String UPDATES = "/updates";


    public BotClient(String baseURL) {
        this.webClient = WebClient.create(baseURL);
    }

    public BotClient() {
        this(BaseURL.LOCAL.getUrl());
    }

    public Mono<String> sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        return webClient.post()
            .uri(UPDATES)
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> {
                    if (UPDATE_EXCEPTION.equals(errorResponse.getExceptionName())) {
                        return Mono.error(new UpdateAlreadyExistsException(errorResponse.getExceptionMessage()));
                    } else  {
                        return Mono.error(new RuntimeException(NOT_VALID_DATA));
                    }
                }))
            .bodyToMono(String.class);
    }
}
