package edu.java.bot.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Set;
import static edu.java.bot.service.DefaultLinkService.addLink;
import static edu.java.bot.service.DefaultLinkService.deleteLink;
import static edu.java.bot.service.DefaultLinkService.getAllLinksOfUser;
import static org.assertj.core.api.Assertions.*;

public class LinkServiceTest {
    @Test
    @DisplayName("Test get links by chat id")
    public void getAllLinksById() {
        String link1 = "https://github.com/alexkorep04/Course";
        String link2 = "https://github.com/alexkorep04/OOP";
        String link3 = "https://github.com/alexkorep04/Tin";
        addLink(0L, link1);
        addLink(0L, link2);
        addLink(0L, link3);
        addLink(-1L, link3);
        Set<String> links1 = getAllLinksOfUser(0L);
        Set<String> links2 = getAllLinksOfUser(-1L);

        assertThat(links1).containsExactlyInAnyOrder(link1, link2, link3);
        assertThat(links2).containsExactlyInAnyOrder(link3);
    }


    @Test
    @DisplayName("Test add link")
    public void testAddLink() {
        String link = "https://github.com/alexkorep04/Course";
        addLink(0L, link);
        Set<String> links1 = getAllLinksOfUser(0L);
        Set<String> links2 = getAllLinksOfUser(-1L);

        assertThat(links1.contains(link)).isTrue();
        assertThat(links2.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test delete link")
    public void testDeleteLink() {
        String link1 = "https://github.com/alexkorep04/Course";
        String link2 = "https://github.com/alexkorep04/OOP";
        addLink(0L, link1);
        addLink(0L, link2);
        addLink(-1L, link2);
        deleteLink(0L, link1);
        deleteLink(-1L, link2);
        Set<String> links1 = getAllLinksOfUser(0L);
        Set<String> links2 = getAllLinksOfUser(-1L);

        assertThat(links1.contains(link1)).isFalse();
        assertThat(links1.contains(link2)).isTrue();
        assertThat(links2.isEmpty()).isTrue();
    }
}
