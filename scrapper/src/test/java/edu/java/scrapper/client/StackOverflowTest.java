package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.client.DefaultStackOverflowClient;
import edu.java.client.StackOverflowClient;
import edu.java.entity.StackOverflowItem;
import edu.java.entity.StackOverflowResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Mono;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class StackOverflowTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    private StackOverflowClient stackOverflowClient;

    @Before
    public void init() {
        String baseUrl = wireMockRule.baseUrl();
        stackOverflowClient = new DefaultStackOverflowClient(baseUrl);
    }

    @Test
    @DisplayName("Test response from stack overflow")
    public void testStackOverflowResponse() {
        stubFor(get(urlEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "items": [
                        {
                          "view_count": 3,
                          "answer_count": 2,
                          "last_activity_date": 1708003813,
                          "question_id": 1,
                          "link": "https://stackoverflow.com/questions/1/hello",
                          "title": "Java question"
                        }
                      ]
                    }""")));
        Mono<StackOverflowResponse> responseMono = stackOverflowClient.fetchQuestion(1);
        StackOverflowItem response = responseMono.block().getItems().getFirst();
        assertThat(response).isNotNull();
        assertThat(1).isEqualTo(response.getId());
        assertThat(3).isEqualTo(response.getViewCount());
        assertThat(2).isEqualTo(response.getAnswerCount());
        assertThat("https://stackoverflow.com/questions/1/hello").isEqualTo(response.getLink());
        assertThat("Java question").isEqualTo(response.getTitle());
    }
}
