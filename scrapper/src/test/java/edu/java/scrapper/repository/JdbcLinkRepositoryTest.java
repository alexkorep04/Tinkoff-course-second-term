package edu.java.scrapper.repository;

import edu.java.dto.ChatLink;
import edu.java.dto.Link;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JdbcLinkRepositoryTest extends IntegrationTest {
    private static final JdbcTemplate jdbcTemplate= new JdbcTemplate(DataSourceBuilder
        .create()
        .url(POSTGRES.getJdbcUrl())
        .username(POSTGRES.getUsername())
        .password(POSTGRES.getPassword())
        .build());
    private final ChatLinkRepository chatLinkRepository = new JdbcChatLinkRepository(jdbcTemplate);
    private final ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
    private final LinkRepository linkRepository = new JdbcLinkRepository(jdbcTemplate);

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test add method")
    public void testAdd() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");
        List<Link> links = linkRepository.findAll();

        assertThat("helloworld1").isEqualTo(links.getFirst().getName());
        assertThat("helloworld2").isEqualTo(links.get(1).getName());
        assertThat("helloworld3").isEqualTo(links.getLast().getName());

        List<ChatLink> chatLinks = chatLinkRepository.findAll();

        assertThat(1L).isEqualTo(chatLinks.getFirst().getChatId());
        assertThat(8L).isEqualTo(chatLinks.getFirst().getLinkId());
        assertThat(1L).isEqualTo(chatLinks.get(1).getChatId());
        assertThat(9L).isEqualTo(chatLinks.get(1).getLinkId());
        assertThat(2L).isEqualTo(chatLinks.getLast().getChatId());
        assertThat(10L).isEqualTo(chatLinks.getLast().getLinkId());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findAll method")
    public void testFindAll() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");

        List<Link> links = linkRepository.findAll();

        assertThat(3).isEqualTo(links.size());
        assertThat("helloworld1").isEqualTo(links.getFirst().getName());
        assertThat("helloworld2").isEqualTo(links.get(1).getName());
        assertThat("helloworld3").isEqualTo(links.getLast().getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test remove method")
    public void testRemove() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");
        linkRepository.remove(1L, "helloworld2");

        List<Link> links = linkRepository.findAll();
        assertThat("helloworld1").isEqualTo(links.getFirst().getName());
        assertThat("helloworld3").isEqualTo(links.getLast().getName());

        List<ChatLink> chatLinks = chatLinkRepository.findAll();
        assertThat(1L).isEqualTo(chatLinks.getFirst().getChatId());
        assertThat(32L).isEqualTo(chatLinks.getFirst().getLinkId());
        assertThat(2L).isEqualTo(chatLinks.get(1).getChatId());
        assertThat(34L).isEqualTo(chatLinks.get(1).getLinkId());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findALlById method")
    public void testFindAllByChatId() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");

        List<Link> links = linkRepository.findAllLinksById(1L);

        assertThat(2).isEqualTo(links.size());
        assertThat("helloworld1").isEqualTo(links.getFirst().getName());
        assertThat("helloworld2").isEqualTo(links.get(1).getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findByChatIdAndUrl method")
    public void testFindByChatIdAndUrl() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");

        Optional<Link> link = linkRepository.findByChatIdAndUrl(1L, "helloworld2");

        assertThat(link).isNotEmpty();
        assertThat("helloworld2").isEqualTo(link.get().getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findById method")
    public void testFindById() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");

        Optional<Link> link = linkRepository.findById(26L);

        assertThat(link).isNotEmpty();
        assertThat("helloworld2").isEqualTo(link.get().getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findChatsByLink method")
    public void testFindChatsByLink() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");
        linkRepository.add(3L, "helloworld1");

        List<Long> chats = linkRepository.findChatsByLink("helloworld1");

        assertThat(2).isEqualTo(chats.size());
        assertThat(1L).isEqualTo(chats.getFirst());
        assertThat(3L).isEqualTo(chats.getLast());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test updateLastCheck method")
    public void testUpdateLastCheck() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");
        linkRepository.add(3L, "helloworld1");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastCheck(dateTime, "helloworld1");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "helloworld1");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "helloworld1");

        assertThat(link1).isNotEmpty();
        assertThat(link2).isNotEmpty();
        assertThat(dateTime.toLocalDate()).isEqualTo(link1.get().getLastCheck().toLocalDate());
        assertThat(dateTime.toLocalDate()).isEqualTo(link2.get().getLastCheck().toLocalDate());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test updateLastUpdate method")
    public void testUpdateLastUpdate() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, "helloworld1");
        linkRepository.add(1L, "helloworld2");
        linkRepository.add(2L, "helloworld3");
        linkRepository.add(3L, "helloworld1");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastUpdate(dateTime, "helloworld1");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "helloworld1");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "helloworld1");

        assertThat(link1).isNotEmpty();
        assertThat(link2).isNotEmpty();
        assertThat(dateTime.toLocalDate()).isEqualTo(link1.get().getLastUpdate().toLocalDate());
        assertThat(dateTime.toLocalDate()).isEqualTo(link2.get().getLastUpdate().toLocalDate());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test findOldestLinks method")
    public void testFindOldestLinks() throws InterruptedException {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, "helloworld1");
        Thread.sleep(50);
        linkRepository.add(1L, "helloworld2");
        Thread.sleep(50);
        linkRepository.add(2L, "helloworld3");
        Thread.sleep(50);
        linkRepository.add(3L, "helloworld1");
        linkRepository.updateLastCheck(OffsetDateTime.MIN, "helloworld3");
        List<Link> links = linkRepository.findOldestLinks(2);

        assertThat(2).isEqualTo(links.size());
        assertThat("helloworld3").isEqualTo(links.getFirst().getName());
        assertThat("helloworld1").isEqualTo(links.getLast().getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }
}
