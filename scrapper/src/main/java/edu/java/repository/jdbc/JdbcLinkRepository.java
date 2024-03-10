package edu.java.repository.jdbc;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final static String NAME = "link_name";
    private final static String CHECK = "last_check";
    private final static String UPDATE = "last_update";
    private final static String ID = "link_id";

    @Override
    @Transactional
    public Link add(long chatId, String linkName) {
        jdbcTemplate.update("INSERT INTO link(link_name, last_update) VALUES (?, ?)",
            linkName, OffsetDateTime.MIN);
        long id = findAll().getLast().getId();
        jdbcTemplate.update("INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)", chatId, id);
        return findById(id).get();
    }

    @Override
    @Transactional
    public Link remove(long chatId, String linkName) {
        Optional<Link> link = findByChatIdAndUrl(chatId, linkName);
        long id = link.get().getId();
        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = (?) AND link_id = (?)", chatId, id);
        jdbcTemplate.update("DELETE FROM link WHERE link_id = (?)", id);
        return link.get();
    }

    @Override
    public List<Link> findAllLinksById(long chatId) {
        return jdbcTemplate.query("SELECT * FROM link WHERE link_id IN"
                + "(SELECT link_id FROM chat_link WHERE chat_id = (?))",
            (resultSet, row) ->
                new Link(resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getObject(CHECK,
                        OffsetDateTime.class),
                    resultSet.getObject(UPDATE,
                        OffsetDateTime.class)), chatId);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link",
            (resultSet, row) ->
                new Link(resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getObject(CHECK,
                        OffsetDateTime.class),
                    resultSet.getObject(UPDATE, OffsetDateTime.class)));
    }

    @Override
    public Optional<Link> findByChatIdAndUrl(Long chatId, String linkName) {
        List<Link> links = jdbcTemplate.query("SELECT DISTINCT * FROM chat_link c JOIN link l "
            + "ON c.link_id = l.link_id WHERE c.chat_id = ?"
            + " AND l.link_name = ?", (resultSet, row) ->
            new Link(resultSet.getLong(ID),
                resultSet.getString(NAME),
                resultSet.getObject(CHECK,
                    OffsetDateTime.class),
                resultSet.getObject(UPDATE, OffsetDateTime.class)), chatId, linkName);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.getFirst());
    }

    @Override
    public Optional<Link> findById(long linkId) {
        List<Link> links = jdbcTemplate.query("SELECT * FROM link WHERE link_id = ?",
            (resultSet, row) ->
                new Link(resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getObject(CHECK,
                        OffsetDateTime.class),
                    resultSet.getObject(UPDATE, OffsetDateTime.class)), linkId);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.getFirst());
    }

    @Override
    public void updateLastCheck(OffsetDateTime newCheck, String name) {
        jdbcTemplate.update("UPDATE link SET last_check = (?) WHERE link_name = (?)", newCheck, name);
    }

    @Override
    public void updateLastUpdate(OffsetDateTime update, String name) {
        jdbcTemplate.update("UPDATE link SET last_update = (?) WHERE link_name = (?)", update, name);
    }

    @Override
    public List<Long> findChatsByLink(String name) {
        return jdbcTemplate.query("SELECT DISTINCT c.chat_id FROM chat_link c "
            + "JOIN link l ON l.link_id = c.link_id WHERE l.link_name = (?)", (resultSet, row) ->
            resultSet.getLong("chat_id"), name);
    }

    @Override
    public List<Link> findOldestLinks(int amount) {
        return jdbcTemplate.query("SELECT * FROM link ORDER BY last_check LIMIT (?)",
            (resultSet, row) ->
                new Link(resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getObject(CHECK,
                        OffsetDateTime.class),
                    resultSet.getObject(UPDATE, OffsetDateTime.class)), amount);
    }
}