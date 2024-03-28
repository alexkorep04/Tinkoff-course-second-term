package edu.java.bot.cllient;

import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListsLinkResponse;
import edu.java.bot.entity.BaseURL;
import edu.java.bot.exception.ChatAlreadyExistsException;
import edu.java.bot.exception.LinkAlreadyExistsException;
import edu.java.bot.exception.NoChatException;
import edu.java.bot.exception.NoResourceException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperClient {
    private final WebClient webClient;
    private final Retry retry;
    private final static String TG_CHAT = "/tg-chat/";
    private final static String LINKS = "links";
    private final static String HEADER = "Tg-Chat-Id";
    private final static String INVALID_ID = "Invalid id!";
    private final static String LINK_EXISTS_EXCEPTION = "edu.java.exception.LinkAlreadyExistsException";
    private final static String METHOD_VALIDATION_EXCEPTION
        = "org.springframework.web.method.annotation.HandlerMethodValidationException";

    public ScrapperClient(Retry retry, String baseURL) {
        this.webClient = WebClient.create(baseURL);
        this.retry = retry;
    }

    public ScrapperClient(Retry retry) {
        this(retry, BaseURL.LOCAL.getUrl());
    }

    public Mono<String> registerChat(long id) {
        String path = TG_CHAT + id;
        return webClient.post()
            .uri(path)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(ApiErrorResponse.class)
            .flatMap(errorResponse -> {
                if (METHOD_VALIDATION_EXCEPTION.equals(errorResponse.getExceptionName())) {
                    return Mono.error(new RuntimeException(INVALID_ID));
                } else  {
                    return Mono.error(new ChatAlreadyExistsException(errorResponse.getExceptionMessage()));
                }
            }))
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(String.class)
            .retryWhen(retry);
    }

    public Mono<String> deleteChat(long id) {
        String path = TG_CHAT + id;
        return webClient.delete()
            .uri(path)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> {
                    if (METHOD_VALIDATION_EXCEPTION.equals(errorResponse.getExceptionName())) {
                        return Mono.error(new RuntimeException(INVALID_ID));
                    } else  {
                        return Mono.error(new NoChatException(errorResponse.getExceptionMessage()));
                    }
                }))
            .onStatus(HttpStatus.NOT_FOUND::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> Mono.error(new NoResourceException(errorResponse.getExceptionMessage()))))
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(String.class)
            .retryWhen(retry);
    }

    public Mono<LinkResponse> addLink(long id, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS)
            .body(BodyInserters.fromValue(addLinkRequest))
            .header(HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> {
                    if (LINK_EXISTS_EXCEPTION.equals(errorResponse.getExceptionName())) {
                        return Mono.error(new LinkAlreadyExistsException(errorResponse.getExceptionMessage()));
                    } else if (METHOD_VALIDATION_EXCEPTION.equals(errorResponse.getExceptionName())) {
                        return Mono.error(new RuntimeException(INVALID_ID));
                    } else {
                        return Mono.error(new NoChatException(errorResponse.getExceptionMessage()));
                    }
                }))
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry);
    }

    public Mono<LinkResponse> deleteLink(long id, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS)
            .body(BodyInserters.fromValue(removeLinkRequest))
            .header(HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> {
                    if (METHOD_VALIDATION_EXCEPTION.equals(errorResponse.getExceptionName())) {
                        return Mono.error(new RuntimeException(INVALID_ID));
                    } else {
                        return Mono.error(new NoChatException(errorResponse.getExceptionMessage()));
                    }
                }))
            .onStatus(HttpStatus.NOT_FOUND::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> Mono.error(new NoResourceException(errorResponse.getExceptionMessage()))))
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry);
    }

    public Mono<ListsLinkResponse> getLinks(long id) {
        return webClient.get()
            .uri(LINKS)
            .header(HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(errorResponse -> {
                    if (METHOD_VALIDATION_EXCEPTION.equals(errorResponse.getExceptionName())) {
                        return Mono.error(new RuntimeException(INVALID_ID));
                    } else {
                        return Mono.error(new NoChatException(errorResponse.getExceptionMessage()));
                    }
                }))
            .onStatus(HttpStatusCode::is5xxServerError, response -> response.createException()
                .flatMap(error -> {
                    return Mono.error(new HttpClientErrorException(response.statusCode()));
                })
            )
            .bodyToMono(ListsLinkResponse.class)
            .retryWhen(retry);
    }
}
