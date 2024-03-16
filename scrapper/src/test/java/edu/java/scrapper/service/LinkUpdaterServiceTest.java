package edu.java.scrapper.service;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import edu.java.scheduler.GitHubLinkUpdater;
import edu.java.scheduler.StackOverflowLinkUpdater;
import edu.java.service.DefaultLinkUpdaterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LinkUpdaterServiceTest {
    @Test
    @DisplayName("Test update method")
    public void testUpdate() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        GitHubLinkUpdater gitHubLinkUpdater = mock(GitHubLinkUpdater.class);
        StackOverflowLinkUpdater stackOverflowLinkUpdater = mock(StackOverflowLinkUpdater.class);
        Link link1 = new Link(1L, "https://github.com/alexkorep04/Course", null, null, null, 0, "GitHubLink");
        Link link2 = new Link(2L, "https://stackoverflow.com/questions/1", null, null, null, 0, "GitHubLink");
        Link link3 = new Link(3L, "https://github.com/alexkorep04/Tinkoff-course-second-term", null, null, null, 0, "StackOverflowLink");
        DefaultLinkUpdaterService linkUpdaterService = new DefaultLinkUpdaterService(linkRepository, gitHubLinkUpdater, stackOverflowLinkUpdater);
        when(linkRepository.findOldestLinks(3)).thenReturn(List.of(link1, link2, link3));
        when(gitHubLinkUpdater.supports(any())).thenReturn(true);
        when(gitHubLinkUpdater.update(any())).thenReturn(1);
        when(stackOverflowLinkUpdater.supports(any())).thenReturn(false);
        int response = linkUpdaterService.update();

        assertThat(2).isEqualTo(response);
    }
}
