package edu.java.repository.jpa;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DefaultJpaLinkRepository implements LinkRepository {
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    @Transactional
    public Link add(long chatId, String linkName) {
        Optional<Link> link = jpaLinkRepository.findByName(linkName);
        String type;
        if (linkName.startsWith("https://github.com/")) {
            type = "GitHubLink";
        } else {
            type = "StackOverflowLink";
        }
        Link newLink;
        if (link.isEmpty()) {
            newLink = new Link();
            newLink.setName(linkName);
            newLink.setLastCheck(OffsetDateTime.now());
            newLink.setType(type);
            newLink = jpaLinkRepository.save(newLink);
        } else {
            newLink = link.get();
        }
        jpaLinkRepository.addLinkToChatLink(chatId, newLink.getId());
        return newLink;
    }

    @Override
    @Transactional
    public Link remove(long chatId, String linkName) {
        Optional<Link> link = findByChatIdAndUrl(chatId, linkName);
        long id = link.get().getId();
        jpaLinkRepository.deleteFromChatLink(chatId, id);
        List<Long> chats = findChatsByLink(linkName);
        if (chats.isEmpty()) {
            jpaLinkRepository.deleteLinkById(id);
        }
        return link.get();
    }

    @Override
    public List<Link> findAllLinksById(long chatId) {
        return jpaLinkRepository.findAllLinksByChatId(chatId);
    }

    @Override
    public List<Link> findAll() {
        return jpaLinkRepository.findAll();
    }

    @Override
    public Optional<Link> findByChatIdAndUrl(Long chatId, String linkName) {
        List<Link> links = jpaLinkRepository.findByChatIdAndUrl(chatId, linkName);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.getFirst());
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return jpaLinkRepository.findById(linkId);
    }

    @Override
    @Transactional
    public void updateLastCheck(OffsetDateTime newCheck, String name) {
        Link link = jpaLinkRepository.findByName(name).get();
        link.setLastCheck(newCheck);
        jpaLinkRepository.save(link);
    }

    @Override
    @Transactional
    public void updateLastUpdate(OffsetDateTime update, String name) {
        Link link = jpaLinkRepository.findByName(name).get();
        link.setLastUpdate(update);
        jpaLinkRepository.save(link);
    }

    @Override
    @Transactional
    public void updateLastCommit(OffsetDateTime commit, String name) {
        Link link = jpaLinkRepository.findByName(name).get();
        link.setLastCommit(commit);
        jpaLinkRepository.save(link);
    }

    @Override
    @Transactional
    public void updateAmountOfIssues(int amountOfPR, String name) {
        Link link = jpaLinkRepository.findByName(name).get();
        link.setAmountOfIssues(amountOfPR);
        jpaLinkRepository.save(link);
    }

    @Override
    public List<Long> findChatsByLink(String name) {
        return jpaLinkRepository.findChatsByLink(name);
    }

    @Override
    public List<Link> findOldestLinks(int amount) {
        return jpaLinkRepository.findTopByOrderByLastCheck(amount);
    }
}
