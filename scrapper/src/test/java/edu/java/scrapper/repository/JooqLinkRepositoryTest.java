package edu.java.scrapper.repository;

import edu.java.dto.Link;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JooqLinkRepositoryTest extends IntegrationTest {
    private final ChatRepository chatRepository = new JooqChatRepository(dslContext);
    private final LinkRepository linkRepository = new JooqLinkRepository(dslContext);

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test add method")
    public void testAdd() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        List<Link> links = linkRepository.findAll();

        assertThat("https://github.com/alexkorep04/Course").isEqualTo(links.getFirst().getName());
        assertThat("https://github.com/alexkorep04/Tinkoff-course-second-term").isEqualTo(links.get(1).getName());
        assertThat("https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith").isEqualTo(links.getLast().getName());
        assertThat("GitHubLink").isEqualTo(links.getFirst().getType());
        assertThat("GitHubLink").isEqualTo(links.get(1).getType());
        assertThat("StackOverflowLink").isEqualTo(links.getLast().getType());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");

        List<Link> links = linkRepository.findAll();

        assertThat(3).isEqualTo(links.size());
        assertThat("https://github.com/alexkorep04/Course").isEqualTo(links.getFirst().getName());
        assertThat("https://github.com/alexkorep04/Tinkoff-course-second-term").isEqualTo(links.get(1).getName());
        assertThat("https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith").isEqualTo(links.getLast().getName());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        linkRepository.remove(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");

        List<Link> links = linkRepository.findAll();
        assertThat("https://github.com/alexkorep04/Course").isEqualTo(links.getFirst().getName());
        assertThat("https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith").isEqualTo(links.getLast().getName());
        assertThat("GitHubLink").isEqualTo(links.getFirst().getType());
        assertThat("StackOverflowLink").isEqualTo(links.getLast().getType());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");

        List<Link> links = linkRepository.findAllLinksById(1L);

        assertThat(2).isEqualTo(links.size());
        assertThat("https://github.com/alexkorep04/Course").isEqualTo(links.getFirst().getName());
        assertThat("https://github.com/alexkorep04/Tinkoff-course-second-term").isEqualTo(links.get(1).getName());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");

        Optional<Link> link = linkRepository.findByChatIdAndUrl(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");

        assertThat(link).isNotEmpty();
        assertThat("https://github.com/alexkorep04/Tinkoff-course-second-term").isEqualTo(link.get().getName());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");

        Optional<Link> link = linkRepository.findById(2L);
        List<Link> links = linkRepository.findAll();
        assertThat(link).isNotEmpty();
        assertThat("https://github.com/alexkorep04/Tinkoff-course-second-term").isEqualTo(link.get().getName());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        linkRepository.add(3L, "https://github.com/alexkorep04/Course");

        List<Long> chats = linkRepository.findChatsByLink("https://github.com/alexkorep04/Course");

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        linkRepository.add(3L, "https://github.com/alexkorep04/Course");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastCheck(dateTime, "https://github.com/alexkorep04/Course");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "https://github.com/alexkorep04/Course");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "https://github.com/alexkorep04/Course");

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        linkRepository.add(3L, "https://github.com/alexkorep04/Course");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastUpdate(dateTime, "https://github.com/alexkorep04/Course");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "https://github.com/alexkorep04/Course");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "https://github.com/alexkorep04/Course");

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
    @DisplayName("Test updateLastCommit method")
    public void testUpdateLastCommit() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        linkRepository.add(3L, "https://github.com/alexkorep04/Course");

        OffsetDateTime dateTime = OffsetDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        linkRepository.updateLastCommit(dateTime, "https://github.com/alexkorep04/Course");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "https://github.com/alexkorep04/Course");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "https://github.com/alexkorep04/Course");

        assertThat(link1).isNotEmpty();
        assertThat(link2).isNotEmpty();
        assertThat(dateTime.toLocalDate()).isEqualTo(link1.get().getLastCommit().toLocalDate());
        assertThat(dateTime.toLocalDate()).isEqualTo(link2.get().getLastCommit().toLocalDate());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test updateIssues method")
    public void testUpdateIssues() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        linkRepository.add(3L, "https://github.com/alexkorep04/Course");
        linkRepository.updateAmountOfIssues(2, "https://github.com/alexkorep04/Course");
        Optional<Link> link1 = linkRepository.findByChatIdAndUrl(1L, "https://github.com/alexkorep04/Course");
        Optional<Link> link2 = linkRepository.findByChatIdAndUrl(3L, "https://github.com/alexkorep04/Course");

        assertThat(link1).isNotEmpty();
        assertThat(link2).isNotEmpty();
        assertThat(2).isEqualTo(link1.get().getAmountOfIssues());
        assertThat(2).isEqualTo(link2.get().getAmountOfIssues());

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
        linkRepository.add(1L, "https://github.com/alexkorep04/Course");
        Thread.sleep(50);
        linkRepository.add(1L, "https://github.com/alexkorep04/Tinkoff-course-second-term");
        Thread.sleep(50);
        linkRepository.add(2L, "https://stackoverflow.com/questions/78155902/how-do-i-incrementally-migrate-from-osgi-to-a-spring-boot-monolith");
        Thread.sleep(50);
        linkRepository.add(3L, "https://github.com/alexkorep04/Course");
        List<Link> links = linkRepository.findOldestLinks(2);

        assertThat(2).isEqualTo(links.size());
        assertThat("https://github.com/alexkorep04/Tinkoff-course-second-term").isEqualTo(links.getLast().getName());
        assertThat("https://github.com/alexkorep04/Course").isEqualTo(links.getFirst().getName());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
        chatRepository.remove(3L);
    }
}
