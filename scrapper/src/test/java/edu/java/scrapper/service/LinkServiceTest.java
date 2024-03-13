package edu.java.scrapper.service;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.exception.LinkAlreadyExistsException;
import edu.java.exception.NoChatException;
import edu.java.exception.NoResourceException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.service.DefaultLinkService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LinkServiceTest {
    @Test
    @DisplayName("Test normal add link")
    public void testAddLink() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(new Chat()));
        when(linkRepository.findByChatIdAndUrl(1L, uri.toString())).thenReturn(Optional.empty());
        Link expected = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        when(linkRepository.add(1L, uri.toString())).thenReturn(expected);
        Link response = defaultLinkService.add(1L, uri);

        assertThat(expected).isEqualTo(response);

        verify(chatRepository).findById(1L);
        verify(linkRepository).findByChatIdAndUrl(1L, uri.toString());
        verify(linkRepository).add(1L, uri.toString());
    }

    @Test
    @DisplayName("Test add link when no chat")
    public void testAddLinkWhenNoChat() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        Link expected = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(linkRepository.findByChatIdAndUrl(1L, uri.toString())).thenReturn(Optional.empty());
        when(linkRepository.add(1L, uri.toString())).thenReturn(expected);

        assertThrows(NoChatException.class, () -> {
            defaultLinkService.add(1L, uri);
        });

        verify(chatRepository).findById(1L);
    }

    @Test
    @DisplayName("Test add link when link exists")
    public void testAddLinkWhenLinkExists() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        Link expected = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(new Chat()));
        when(linkRepository.findByChatIdAndUrl(1L, uri.toString())).thenReturn(Optional.of(
            new Link(1L, "https://github.com/alexkorep04", OffsetDateTime.MIN, OffsetDateTime.MIN)));
        when(linkRepository.add(1L, uri.toString())).thenReturn(expected);

        assertThrows(LinkAlreadyExistsException.class, () -> {
            defaultLinkService.add(1L, uri);
        });

        verify(chatRepository).findById(1L);
        verify(linkRepository).findByChatIdAndUrl(1L, uri.toString());
    }

    @Test
    @DisplayName("Test normal remove link")
    public void testRemoveLink() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        Link expected = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(new Chat()));
        when(linkRepository.findByChatIdAndUrl(1L, uri.toString())).thenReturn(Optional.of(
            expected));
        when(linkRepository.remove(1L, uri.toString())).thenReturn(expected);
        Link response = defaultLinkService.remove(1L, uri);

        assertThat(expected).isEqualTo(response);

        verify(chatRepository).findById(1L);
        verify(linkRepository).findByChatIdAndUrl(1L, uri.toString());
        verify(linkRepository).remove(1L, uri.toString());
    }

    @Test
    @DisplayName("Test remove link when no chat")
    public void testRemoveLinkWhenNoChat() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        Link expected = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(linkRepository.findByChatIdAndUrl(1L, uri.toString())).thenReturn(Optional.empty());
        when(linkRepository.remove(1L, uri.toString())).thenReturn(expected);

        assertThrows(NoChatException.class, () -> {
            defaultLinkService.remove(1L, uri);
        });

        verify(chatRepository).findById(1L);
    }

    @Test
    @DisplayName("Test remove when no link")
    public void testRemoveLinkWhenNoLink() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(new Chat()));
        when(linkRepository.findByChatIdAndUrl(1L, uri.toString())).thenReturn(Optional.empty());
        Link expected = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        when(linkRepository.remove(1L, uri.toString())).thenReturn(expected);
        assertThrows(NoResourceException.class, () -> {
            defaultLinkService.remove(1L, uri);
        });

        verify(chatRepository).findById(1L);
        verify(linkRepository).findByChatIdAndUrl(1L, uri.toString());
    }

    @Test
    @DisplayName("Test normal get links")
    public void testGetLinks() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri1 = URI.create("https://github.com/alexkorep04");
        URI uri2 = URI.create("https://github.com/alexkorep04/Course");
        Link expected1 = new Link(1L, uri1.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        Link expected2 = new Link(1L, uri2.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(new Chat()));
        when(linkRepository.findAllLinksById(1L)).thenReturn(List.of(expected1, expected2));

        List<Link> response = defaultLinkService.listAll(1L);
        assertThat(2).isEqualTo(response.size());
        assertThat(expected1).isEqualTo(response.getFirst());
        assertThat(expected2).isEqualTo(response.getLast());

        verify(chatRepository).findById(1L);
        verify(linkRepository).findAllLinksById(1L);
    }

    @Test
    @DisplayName("Test get links when chat does not exist")
    public void testGetLinksWhenNoChat() {
        LinkRepository linkRepository = mock(LinkRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        URI uri = URI.create("https://github.com/alexkorep04");
        Link link = new Link(1L, uri.toString(), OffsetDateTime.MIN, OffsetDateTime.MIN);
        DefaultLinkService defaultLinkService = new DefaultLinkService(linkRepository, chatRepository);
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());
        when(linkRepository.findAllLinksById(1L)).thenReturn(List.of(link));
        assertThrows(NoChatException.class, () ->
                defaultLinkService.listAll(1L)
        );

        verify(chatRepository).findById(1L);
    }
}
