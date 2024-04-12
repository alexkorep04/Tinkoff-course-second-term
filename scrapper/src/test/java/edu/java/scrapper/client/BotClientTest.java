package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.client.BotClient;
import edu.java.dto.request.LinkUpdateRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BotClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    private BotClient botClient;

    @Before
    public void init() {
        botClient = new BotClient("http://localhost:8080");
    }

    @Test
    @DisplayName("Test successful update")
    public void testSuccessfulUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Обновление обработано")));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(1L, URI.create("https://github.com/alexkorep04/Course"), "Test...", List.of(-1L, 0L));
        Mono<String> responseMono = botClient.sendUpdate(linkUpdateRequest);
        String response = responseMono.block();

        assertThat("Обновление обработано").isEqualTo(response);
    }
    @Test
    @DisplayName("Test double update")
    public void testDoubleUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Обновление обработано")));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(1L, URI.create("https://github.com/alexkorep04/Course"), "Test...", List.of(-1L, 0L));
        Mono<String> responseMono1 = botClient.sendUpdate(linkUpdateRequest);
        String response = responseMono1.block();

        assertThat("Обновление обработано").isEqualTo(response);

        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"edu.java.exception.UpdateAlreadyExistsException\"" +
                    ",\"exceptionMessage\":\"Update already exists exception message\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<String> responseMono2 = botClient.sendUpdate(linkUpdateRequest);
        Throwable throwable = assertThrows(RuntimeException.class, responseMono2::block);

        assertThat("Update already exists exception message").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test not valid update")
    public void testNotValidUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"MethodArgumentNotValidException\"" +
                    ",\"exceptionMessage\":\"Not valid data!\"" +
                    ",\"stackTrace\":[1]}")));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(-1L, URI.create("https://github.com/alexkorep04/Course"), "Test...", List.of(-1L, 0L));
        Mono<String> responseMono = botClient.sendUpdate(linkUpdateRequest);
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("Not valid data!").isEqualTo(throwable.getMessage());
    }
}
