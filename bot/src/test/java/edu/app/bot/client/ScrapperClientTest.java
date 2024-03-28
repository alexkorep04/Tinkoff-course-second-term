package edu.app.bot.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListsLinkResponse;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import edu.java.bot.exception.NoResourceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScrapperClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    private ScrapperClient scrapperClient;

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
        Retry retry = getConstantRetry();
        scrapperClient = new ScrapperClient(retry, "http://localhost:8080");
    }

    @Test
    @DisplayName("Test successful registration")
    public void testSuccessfulRegistration() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Чат зарегистрирован")));
        Mono<String> responseMono = scrapperClient.registerChat(1L);
        String response = responseMono.block();

        assertThat("Чат зарегистрирован").isEqualTo(response);
    }

    @Test
    @DisplayName("Test registration when id is illegal")
    public void testRegistrationWithIllegalId() {
        stubFor(post(urlEqualTo("/tg-chat/0"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"MethodArgumentNotValidException\"" +
                    ",\"exceptionMessage\":\"Invalid id!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<String> responseMono = scrapperClient.registerChat(0L);
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("Invalid id!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test when chat already exists")
    public void testChatExists() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Чат зарегистрирован")));
        Mono<String> responseMono1 = scrapperClient.registerChat(1L);
        String response1 = responseMono1.block();

        assertThat("Чат зарегистрирован").isEqualTo(response1);

        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"edu.java.exception.ChatAlreadyExistsException\"" +
                    ",\"exceptionMessage\":\"Chat is already registered!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<String> responseMono2 = scrapperClient.registerChat(1L);
        Throwable throwable = assertThrows(RuntimeException.class, responseMono2::block);

        assertThat("Chat is already registered!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test successful delete of chat")
    public void testSuccessfulDeleteChat() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Чат зарегистрирован")));
        Mono<String> responseMono1 = scrapperClient.registerChat(1L);
        String response1 = responseMono1.block();

        assertThat("Чат зарегистрирован").isEqualTo(response1);

        stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Чат успешно удалён")));

        Mono<String> responseMono2 = scrapperClient.deleteChat(1L);
        String response2 = responseMono2.block();

        assertThat("Чат успешно удалён").isEqualTo(response2);
    }

    @Test
    @DisplayName("Test delete chat when id is illegal")
    public void testDeleteChatWithIllegalId() {
        stubFor(delete(urlEqualTo("/tg-chat/0"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"MethodArgumentNotValidException\"" +
                    ",\"exceptionMessage\":\"Invalid id!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<String> responseMono = scrapperClient.deleteChat(0L);
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("Invalid id!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test delete chat when it does not exist")
    public void testDeleteChatWhenItNotExists() {
        stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Link not found!\"" +
                    ",\"code\":\"404\"" +
                    ",\"exceptionName\":\"edu.java.exception.NoChatException\"" +
                    ",\"exceptionMessage\":\"No such chat in database!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<String> responseMono = scrapperClient.deleteChat(1L);
        Throwable throwable = assertThrows(NoResourceException.class, responseMono::block);

        assertThat("No such chat in database!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test getting links")
    public void testGetLinks() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
            {
                "links":[
                    {
                        "id":1,
                        "url":"string123"
                    }
                ],
                "size":1
            }
            """)));
        Mono<ListsLinkResponse> responseMono = scrapperClient.getLinks(1L);
        ListsLinkResponse linkResponse = responseMono.block();

        assertThat(linkResponse).isNotNull();
        assertThat(1).isEqualTo(linkResponse.getSize());
        assertThat(1).isEqualTo(linkResponse.getLinks().size());
        assertThat(linkResponse.getLinks().getFirst()).isNotNull();
        assertThat(1L).isEqualTo(linkResponse.getLinks().getFirst().getId());
        assertThat("string123").isEqualTo(linkResponse.getLinks().getFirst().getUrl().toString());
    }

    @Test
    @DisplayName("Test getting links with not valid header")
    public void testGetLinksWithNotCorrectHeader() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"org.springframework.web.method.annotation.HandlerMethodValidationException\"" +
                    ",\"exceptionMessage\":\"Invalid id!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<ListsLinkResponse> responseMono = scrapperClient.getLinks(0L);
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("Invalid id!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test add correct link")
    public void testAddCorrectLink() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id":1,
                        "url":"string123"
                    }
            """)));
        Mono<LinkResponse> responseMono = scrapperClient.addLink(1L, new AddLinkRequest(URI.create("string123")));
        LinkResponse response = responseMono.block();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUrl()).isEqualTo(URI.create("string123"));
    }

    @Test
    @DisplayName("Test add link with not correct header")
    public void testAddLinkWithNotValidHeader() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"org.springframework.web.method.annotation.HandlerMethodValidationException\"" +
                    ",\"exceptionMessage\":\"Invalid id!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<LinkResponse> responseMono = scrapperClient.addLink(0L, new AddLinkRequest(URI.create("string123")));
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("Invalid id!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test add double link")
    public void testAddLinkDouble() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id":1,
                        "url":"string123"
                    }
            """)));
        Mono<LinkResponse> responseMono = scrapperClient.addLink(1L, new AddLinkRequest(URI.create("string123")));
        LinkResponse response = responseMono.block();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUrl()).isEqualTo(URI.create("string123"));


        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"edu.java.exception.NoChatException\"" +
                    ",\"exceptionMessage\":\"Link is already in database!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<LinkResponse> responseMono2 = scrapperClient.addLink(1L, new AddLinkRequest(URI.create("string123")));
        Throwable throwable = assertThrows(RuntimeException.class, responseMono2::block);

        assertThat("Link is already in database!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test delete correct link")
    public void testDeleteCorrectLink() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id":1,
                        "url":"string123"
                    }
            """)));
        Mono<LinkResponse> responseMono = scrapperClient.addLink(1L, new AddLinkRequest(URI.create("string123")));
        LinkResponse response = responseMono.block();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUrl()).isEqualTo(URI.create("string123"));


        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id":1,
                        "url":"string123"
                    }
            """)));
        Mono<LinkResponse> responseMono2 = scrapperClient.deleteLink(1L, new RemoveLinkRequest(URI.create("string123")));
        LinkResponse response2 = responseMono2.block();

        assertThat(response2).isNotNull();
        assertThat(response2.getId()).isEqualTo(1L);
        assertThat(response2.getUrl()).isEqualTo(URI.create("string123"));
    }

    @Test
    @DisplayName("Test delete link that not in database")
    public void testDeleteLinkThatDoesNotExist() {
        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Link not found!\"" +
                    ",\"code\":\"404\"" +
                    ",\"exceptionName\":\"edu.java.exception.NoResourceException\"" +
                    ",\"exceptionMessage\":\"No such link in database!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<LinkResponse> responseMono = scrapperClient.deleteLink(1L, new RemoveLinkRequest(URI.create("string123")));
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("No such link in database!").isEqualTo(throwable.getMessage());
    }

    @Test
    @DisplayName("Test delete link with not correct header")
    public void testDeleteLinkWithNotValidHeader() {
        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Wrong parameters!\"" +
                    ",\"code\":\"400\"" +
                    ",\"exceptionName\":\"org.springframework.web.method.annotation.HandlerMethodValidationException\"" +
                    ",\"exceptionMessage\":\"Invalid id!\"" +
                    ",\"stackTrace\":[1]}")));
        Mono<LinkResponse> responseMono = scrapperClient.deleteLink(0L, new RemoveLinkRequest(URI.create("string123")));
        Throwable throwable = assertThrows(RuntimeException.class, responseMono::block);

        assertThat("Invalid id!").isEqualTo(throwable.getMessage());
    }
}
