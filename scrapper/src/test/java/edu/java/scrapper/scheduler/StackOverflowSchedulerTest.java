package edu.java.scrapper.scheduler;

import edu.java.client.BotClient;
import edu.java.client.DefaultStackOverflowClient;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.dto.Link;
import edu.java.entity.GitHubResponse;
import edu.java.entity.StackOverflowItem;
import edu.java.entity.StackOverflowResponse;
import edu.java.repository.LinkRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scheduler.GitHubLinkUpdater;
import edu.java.scheduler.StackOverflowLinkUpdater;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StackOverflowSchedulerTest {
    @Test
    @DisplayName("Test supports method")
    public void testSupports() {
        DefaultStackOverflowClient stackOverflowClient = mock(DefaultStackOverflowClient.class);
        LinkRepository linkRepository = mock(JdbcLinkRepository.class);
        BotClient botWebClient = mock(BotClient.class);
        StackOverflowLinkUpdater stackOverflowLinkUpdater = new StackOverflowLinkUpdater(botWebClient, linkRepository, stackOverflowClient);

        assertThat(stackOverflowLinkUpdater.supports("https://stackoverflow.com/questions/78131145/multiple-conditions-on-a-dataset-in-spark")).isTrue();
        assertThat(stackOverflowLinkUpdater.supports("https://vk.com")).isFalse();
    }

    @Test
    @DisplayName("Test update in first time method")
    public void testUpdateFirstTime() {
        StackOverflowClient stackOverflowClient = mock(DefaultStackOverflowClient.class);
        LinkRepository linkRepository = mock(JdbcLinkRepository.class);
        BotClient botWebClient = mock(BotClient.class);
        Link link = new Link(1L, "https://stackoverflow.com/questions/1/f", OffsetDateTime.MIN,
            null, null, 0, "StackOverflowLink");
        StackOverflowResponse stackOverflowResponse = new StackOverflowResponse(List.of(new StackOverflowItem(1, 1, 1, OffsetDateTime.MAX.minusYears(10),
            "https://stackoverflow.com/questions/1/f", "1")));
        when(stackOverflowClient.fetchQuestion(1)).thenReturn(Mono.just(stackOverflowResponse));
        StackOverflowLinkUpdater stackOverflowLinkUpdater = new StackOverflowLinkUpdater(botWebClient, linkRepository, stackOverflowClient);

        assertThat(0L).isEqualTo(stackOverflowLinkUpdater.update(link));
        verify(stackOverflowClient).fetchQuestion(1);
    }

    @Test
    @DisplayName("Test update method")
    public void testNormalUpdate() {
        StackOverflowClient stackOverflowClient = mock(DefaultStackOverflowClient.class);
        LinkRepository linkRepository = mock(JdbcLinkRepository.class);
        BotClient botWebClient = mock(BotClient.class);
        Link link = new Link(1L, "https://stackoverflow.com/questions/1/f", OffsetDateTime.MIN,
            OffsetDateTime.MAX.minusYears(20), null, 0, "StackOverflowLink");
        StackOverflowResponse stackOverflowResponse = new StackOverflowResponse(List.of(new StackOverflowItem(1, 1, 1, OffsetDateTime.MAX.minusYears(10),
            "https://stackoverflow.com/questions/1/f", "1")));
        when(stackOverflowClient.fetchQuestion(1)).thenReturn(Mono.just(stackOverflowResponse));
        when(linkRepository.findChatsByLink("https://stackoverflow.com/questions/1/f")).thenReturn(List.of(2L));
        when(botWebClient.sendUpdate(any())).thenReturn(Mono.just("Обновление обработано"));
        StackOverflowLinkUpdater stackOverflowLinkUpdater = new StackOverflowLinkUpdater(botWebClient, linkRepository, stackOverflowClient);

        assertThat(1L).isEqualTo(stackOverflowLinkUpdater.update(link));
        verify(stackOverflowClient).fetchQuestion(1);
        verify(linkRepository).findChatsByLink("https://stackoverflow.com/questions/1/f");
    }
}
