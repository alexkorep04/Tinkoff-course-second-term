package edu.java.service;

import edu.java.dto.Link;
import edu.java.exception.LinkAlreadyExistsException;
import edu.java.exception.NoChatException;
import edu.java.exception.NoResourceException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private static final String NO_CHAT = "No such chat in database!";

    @Override
    public Link add(long tgChatId, URI url) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            throw new NoChatException(NO_CHAT);
        }
        Optional<Link> link = linkRepository.findByChatIdAndUrl(tgChatId, url.toString());
        if (link.isPresent()) {
            throw new LinkAlreadyExistsException("Link already exists in database!");
        }
        return linkRepository.add(tgChatId, url.toString());
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            throw new NoChatException(NO_CHAT);
        }
        Optional<Link> link = linkRepository.findByChatIdAndUrl(tgChatId, url.toString());
        if (link.isEmpty()) {
            throw new NoResourceException("No such link in database!");
        }
        return linkRepository.remove(tgChatId, url.toString());
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            throw new NoChatException(NO_CHAT);
        }
        return linkRepository.findAllLinksById(tgChatId);
    }
}
