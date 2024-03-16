package edu.java.repository;

import edu.java.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Link add(long chatId, String linkName);

    Link remove(long chatId, String linkName);

    List<Link> findAllLinksById(long chatId);

    List<Link> findAll();

    Optional<Link> findByChatIdAndUrl(Long chatId, String linkName);

    Optional<Link> findById(long linkId);

    void updateLastCheck(OffsetDateTime newCheck, String name);

    void updateLastUpdate(OffsetDateTime update, String name);

    void updateLastCommit(OffsetDateTime commit, String name);

    void updateAmountOfIssues(int amountOfPR, String name);

    List<Long> findChatsByLink(String name);

    List<Link> findOldestLinks(int amount);
}
