package edu.java.scrapper.repository;

import edu.java.dto.Link;
import edu.java.repository.ChatRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
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
        linkRepository.add(1L, "helloworld4");
        linkRepository.add(1L, "helloworld5");
        linkRepository.add(2L, "helloworld6");

        List<Link> links = linkRepository.findAll();

        assertThat(3).isEqualTo(links.size());
        assertThat("helloworld4").isEqualTo(links.getFirst().getName());
        assertThat("helloworld5").isEqualTo(links.get(1).getName());
        assertThat("helloworld6").isEqualTo(links.getLast().getName());

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
        linkRepository.add(1L, "helloworld7");
        linkRepository.add(1L, "helloworld8");
        linkRepository.add(2L, "helloworld9");
        linkRepository.remove(1L, "helloworld8");

        List<Link> links = linkRepository.findAll();
        assertThat("helloworld7").isEqualTo(links.getFirst().getName());
        assertThat("helloworld9").isEqualTo(links.getLast().getName());

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
        linkRepository.add(1L, "helloworld10");
        linkRepository.add(1L, "helloworld11");
        linkRepository.add(2L, "helloworld12");

        List<Link> links = linkRepository.findAllLinksById(1L);

        assertThat(2).isEqualTo(links.size());
        assertThat("helloworld10").isEqualTo(links.getFirst().getName());
        assertThat("helloworld11").isEqualTo(links.get(1).getName());

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
        linkRepository.add(1L, "helloworld13");
        linkRepository.add(1L, "helloworld14");
        linkRepository.add(2L, "helloworld15");

        Optional<Link> link = linkRepository.findByChatIdAndUrl(1L, "helloworld14");

        assertThat(link).isNotEmpty();
        assertThat("helloworld14").isEqualTo(link.get().getName());

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
        linkRepository.add(1L, "helloworld16");
        linkRepository.add(1L, "helloworld17");
        linkRepository.add(2L, "helloworld18");

        Optional<Link> link = linkRepository.findById(23L);

        assertThat(link).isNotEmpty();
        assertThat("helloworld17").isEqualTo(link.get().getName());

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
        linkRepository.add(1L, "helloworld19");
        linkRepository.add(1L, "helloworld20");
        linkRepository.add(2L, "helloworld21");
        linkRepository.add(3L, "helloworld19");

        List<Long> chats = linkRepository.findChatsByLink("helloworld19");

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
        linkRepository.add(1L, "helloworld22");
        linkRepository.add(1L, "helloworld23");
        linkRepository.add(2L, "helloworld24");
        linkRepository.add(3L, "helloworld22");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastCheck(dateTime, "helloworld22");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "helloworld22");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "helloworld22");

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
        linkRepository.add(1L, "helloworld25");
        linkRepository.add(1L, "helloworld26");
        linkRepository.add(2L, "helloworld27");
        linkRepository.add(3L, "helloworld25");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastUpdate(dateTime, "helloworld25");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "helloworld25");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "helloworld25");

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
        linkRepository.add(1L, "helloworld28");
        Thread.sleep(50);
        linkRepository.add(1L, "helloworld29");
        Thread.sleep(50);
        linkRepository.add(2L, "helloworld30");
        Thread.sleep(50);
        linkRepository.add(3L, "helloworld28");
        linkRepository.updateLastCheck(OffsetDateTime.MIN, "helloworld30");
        List<Link> links = linkRepository.findOldestLinks(2);

        assertThat(2).isEqualTo(links.size());
        assertThat("helloworld30").isEqualTo(links.getFirst().getName());
        assertThat("helloworld28").isEqualTo(links.getLast().getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }
}
