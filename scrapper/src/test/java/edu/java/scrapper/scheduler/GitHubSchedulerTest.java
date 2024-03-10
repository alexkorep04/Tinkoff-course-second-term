package edu.java.scrapper.scheduler;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.dto.Link;
import edu.java.entity.GitHubResponse;
import edu.java.repository.LinkRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scheduler.GitHubLinkUpdater;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GitHubSchedulerTest {
    @Test
    @DisplayName("Test supports method")
    public void testSupports() {
        GitHubClient githubClient = mock(GitHubClient.class);
        LinkRepository linkRepository = mock(JdbcLinkRepository.class);
        BotClient botWebClient = mock(BotClient.class);
        GitHubLinkUpdater gitHubLinkUpdater = new GitHubLinkUpdater(botWebClient, linkRepository, githubClient);

        assertThat(gitHubLinkUpdater.supports("https://github.com/alexkorep04/Course")).isTrue();
        assertThat(gitHubLinkUpdater.supports("https://vk.com")).isFalse();
    }

    @Test
    @DisplayName("Test update in first time method")
    public void testUpdateFirstTime() {
        GitHubClient githubClient = mock(GitHubClient.class);
        LinkRepository linkRepository = mock(JdbcLinkRepository.class);
        BotClient botWebClient = mock(BotClient.class);
        Link link = new Link(1L, "https://github.com/alexkorep04/Course", OffsetDateTime.MIN,
            OffsetDateTime.MIN);
        GitHubResponse gitHubResponse = new GitHubResponse(1, "Course", "https://github.com/alexkorep04/Course",
            "1", OffsetDateTime.MIN, OffsetDateTime.MAX);
        String account = "alexkorep04";
        String repository = "Course";
        when(githubClient.fetchRepository(account, repository)).thenReturn(Mono.just(gitHubResponse));
        GitHubLinkUpdater gitHubLinkUpdater = new GitHubLinkUpdater(botWebClient, linkRepository, githubClient);

        assertThat(0L).isEqualTo(gitHubLinkUpdater.update(link));
        verify(githubClient).fetchRepository(account, repository);
    }

    @Test
    @DisplayName("Test update  method")
    public void testNormalUpdate() {
        GitHubClient githubClient = mock(GitHubClient.class);
        LinkRepository linkRepository = mock(JdbcLinkRepository.class);
        BotClient botWebClient = mock(BotClient.class);
        Link link = new Link(1L, "https://github.com/alexkorep04/Course", OffsetDateTime.MIN,
            OffsetDateTime.now());
        GitHubResponse gitHubResponse = new GitHubResponse(1, "Course", "https://github.com/alexkorep04/Course",
            "1", OffsetDateTime.MIN, OffsetDateTime.MAX.minusYears(19));
        String account = "alexkorep04";
        String repository = "Course";
        when(githubClient.fetchRepository(account, repository)).thenReturn(Mono.just(gitHubResponse));
        when(linkRepository.findChatsByLink("https://github.com/alexkorep04/Course")).thenReturn(List.of(2L));
        when(botWebClient.sendUpdate(any())).thenReturn(Mono.just("Обновление обработано"));
        GitHubLinkUpdater gitHubLinkUpdater = new GitHubLinkUpdater(botWebClient, linkRepository, githubClient);

        assertThat(1L).isEqualTo(gitHubLinkUpdater.update(link));
        verify(githubClient).fetchRepository(account, repository);
        verify(linkRepository).findChatsByLink("https://github.com/alexkorep04/Course");
        verify(botWebClient).sendUpdate(any());
    }
}
