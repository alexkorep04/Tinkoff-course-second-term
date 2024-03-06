package edu.java.repository;

import edu.java.dto.Link;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Link add(long chatId, String linkName);

    Link remove(long chatId, String linkName);

    List<Link> findAllLinksById(long chatId);

    List<Link> findAll();

    Optional<Link> findByChatIdAndUrl(Long chatId, String linkName);
}
