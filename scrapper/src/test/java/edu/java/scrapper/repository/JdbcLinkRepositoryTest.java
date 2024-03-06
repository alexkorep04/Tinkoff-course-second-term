package edu.java.scrapper.repository;

import edu.java.dto.ChatLink;
import edu.java.dto.Link;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.DefaultChatLinkRepository;
import edu.java.repository.DefaultChatRepository;
import edu.java.repository.DefaultLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JdbcLinkRepositoryTest extends IntegrationTest {
    private static final JdbcTemplate jdbcTemplate= new JdbcTemplate(DataSourceBuilder
        .create()
        .url(POSTGRES.getJdbcUrl())
        .username(POSTGRES.getUsername())
        .password(POSTGRES.getPassword())
        .build());
    private final ChatLinkRepository chatLinkRepository = new DefaultChatLinkRepository(jdbcTemplate);
    private final ChatRepository chatRepository = new DefaultChatRepository(jdbcTemplate);
    private final LinkRepository linkRepository = new DefaultLinkRepository(jdbcTemplate);

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
        assertThat(1L).isEqualTo(chatLinks.getFirst().getLinkId());
        assertThat(1L).isEqualTo(chatLinks.get(1).getChatId());
        assertThat(2L).isEqualTo(chatLinks.get(1).getLinkId());
        assertThat(2L).isEqualTo(chatLinks.getLast().getChatId());
        assertThat(3L).isEqualTo(chatLinks.getLast().getLinkId());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Test remove method")
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
        assertThat(7L).isEqualTo(chatLinks.getFirst().getLinkId());
        assertThat(2L).isEqualTo(chatLinks.get(1).getChatId());
        assertThat(9L).isEqualTo(chatLinks.get(1).getLinkId());

        chatRepository.remove(1L);
        chatRepository.remove(2L);
    }
}
