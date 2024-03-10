package edu.java.repository.jdbc;

import edu.java.dto.Chat;
import edu.java.repository.ChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(long id) {
        jdbcTemplate.update("INSERT INTO chat(id) VALUES (?)", id);
    }

    @Override
    @Transactional
    public int remove(long id) {
        List<Long> links = jdbcTemplate
            .queryForList("SELECT link_id FROM chat_link WHERE chat_id = (?)", Long.class, id);
        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = (?)", id);
        for (Long linkId: links) {
            jdbcTemplate.update("DELETE FROM link WHERE link_id = (?)", linkId);
        }
        int deletedRows = jdbcTemplate.update("DELETE FROM chat WHERE id = (?)", id);
        return deletedRows;
    }

    @Override
    public Optional<Chat> findById(long id) {
        List<Chat> chats = jdbcTemplate.query("SELECT * FROM chat WHERE id = ?",
            (resultSet, row) -> new Chat(resultSet.getLong("id")), id);
        if (chats.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(chats.getFirst());
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat",
            (resultSet, row) -> new Chat(resultSet.getLong("id")));
    }
}
