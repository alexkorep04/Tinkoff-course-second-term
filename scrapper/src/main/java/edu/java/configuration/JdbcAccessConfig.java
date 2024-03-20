package edu.java.configuration;

import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAccessConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public LinkRepository getLinkRepository() {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public ChatRepository getChatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }
}
