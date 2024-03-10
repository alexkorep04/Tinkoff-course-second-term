package edu.java.repository.jdbc;

import edu.java.dto.ChatLink;
import edu.java.repository.ChatLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String CHAT = "chat_id";
    private static final String LINK = "link_id";

    @Override
    public List<ChatLink> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat_link",
            (resultSet, row) ->
                new ChatLink(resultSet.getLong(CHAT),
                    resultSet.getLong(LINK)
                ));
    }

    @Override
    public List<ChatLink> findById(long chatId) {
        return jdbcTemplate.query("SELECT * FROM chat_link WHERE chat_id = (?)",
            (resultSet, row) ->
                new ChatLink(resultSet.getLong(CHAT),
                    resultSet.getLong(LINK)
                ), chatId);
    }
}

