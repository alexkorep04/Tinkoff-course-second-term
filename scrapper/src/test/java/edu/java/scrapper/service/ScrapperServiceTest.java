package edu.java.scrapper.service;

import edu.java.dto.response.LinkResponse;
import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.LinkAlreadyExistsException;
import edu.java.exception.NoResourceException;
import edu.java.service.DefaultScrapperService;
import edu.java.service.ScrapperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import java.net.URI;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScrapperServiceTest {
    ScrapperService scrapperService;
    @Before
    public void init() {
        scrapperService = new DefaultScrapperService();
    }

    @Test
    @DisplayName("Test registration chat")
    public void testRegistrationChat() {
        assertDoesNotThrow(() -> scrapperService.registerChat(1L));
        assertThrows(ChatAlreadyExistsException.class, () -> scrapperService.registerChat(1L));
    }

    @Test
    @DisplayName("Test delete chat")
    public void testDeleteChat() {
        assertThrows(NoResourceException.class, () -> scrapperService.deleteChat(1L));
        scrapperService.registerChat(1L);
        assertDoesNotThrow(() -> scrapperService.deleteChat(1L));
        assertThrows(NoResourceException.class, () -> scrapperService.deleteChat(1L));
    }

    @Test
    @DisplayName("Test add link")
    public void testAddLink() {
        scrapperService.registerChat(1L);
        assertDoesNotThrow(() -> scrapperService.addLink(1L, URI.create("https://github.com/alexkorep04")));
        assertThrows(LinkAlreadyExistsException.class, () -> scrapperService.addLink(1L, URI.create("https://github.com/alexkorep04")));
        LinkResponse linkResponse = scrapperService.addLink(1L, URI.create("string"));
        assertThat(linkResponse).isNotNull();
    }

    @Test
    @DisplayName("Test delete link")
    public void testDeleteLink() {
        scrapperService.registerChat(1L);
        assertDoesNotThrow(() -> scrapperService.addLink(1L, URI.create("https://github.com/alexkorep04")));
        assertDoesNotThrow(() -> scrapperService.deleteLink(1L, URI.create("https://github.com/alexkorep04")));
        assertThrows(NoResourceException.class, () -> scrapperService.deleteLink(1L, URI.create("https://github.com/alexkorep04")));
        scrapperService.addLink(1L, URI.create("string"));
        LinkResponse linkResponse = scrapperService.deleteLink(1L, URI.create("string"));
        assertThat(linkResponse).isNotNull();
    }

    @Test
    @DisplayName("Test get all links")
    public void testGetLinks() {
        scrapperService.registerChat(1L);
        assertThat(scrapperService.getLinks(1L)).isEmpty();
        LinkResponse linkResponse1 = new LinkResponse(1L,  URI.create("string1"));
        LinkResponse linkResponse2 = new LinkResponse(2L,  URI.create("string2"));
        scrapperService.addLink(1L, URI.create("string1"));
        scrapperService.addLink(1L, URI.create("string2"));
        List<LinkResponse> expected = List.of(linkResponse1, linkResponse2);
        assertThat(expected).isEqualTo(scrapperService.getLinks(1L));
    }
}
