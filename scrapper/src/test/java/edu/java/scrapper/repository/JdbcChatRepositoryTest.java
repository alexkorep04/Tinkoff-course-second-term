package edu.java.scrapper.repository;

import edu.java.dto.Chat;
import edu.java.repository.ChatRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JdbcChatRepositoryTest extends IntegrationTest {
    private static final JdbcTemplate jdbcTemplate= new JdbcTemplate(DataSourceBuilder
        .create()
        .url(POSTGRES.getJdbcUrl())
        .username(POSTGRES.getUsername())
        .password(POSTGRES.getPassword())
        .build());
    private final ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test add method")
    public void testAdd() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        List<Chat> chats = chatRepository.findAll();

        assertThat(1L).isEqualTo(chats.getFirst().getId());
        assertThat(2L).isEqualTo(chats.getLast().getId());
        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test remove method")
    public void testRemove() {
        chatRepository.add(1L);
        List<Chat> chats = chatRepository.findAll();

        assertThat(1L).isEqualTo(chats.getFirst().getId());
        chatRepository.remove(1L);

        List<Chat> newChats = chatRepository.findAll();

        assertThat(newChats.isEmpty()).isTrue();
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findAll method")
    public void testFindAll() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        List<Chat> chats = chatRepository.findAll();

        assertThat(2).isEqualTo(chats.size());
        assertThat(1L).isEqualTo(chats.getFirst().getId());
        assertThat(2L).isEqualTo(chats.getLast().getId());
        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findById method")
    public void testFindById() {
        chatRepository.add(1L);
        Optional<Chat> chat = chatRepository.findById(1L);

        assertThat(chat.isPresent()).isTrue();
        assertThat(1L).isEqualTo(chat.get().getId());

        Optional<Chat> unknownChat = chatRepository.findById(2L);
        assertThat(unknownChat.isEmpty()).isTrue();
        chatRepository.remove(1L);
    }
}
