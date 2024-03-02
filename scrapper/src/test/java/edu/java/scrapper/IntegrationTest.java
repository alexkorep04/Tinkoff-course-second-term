package edu.java.scrapper;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        try {
            runMigrations(POSTGRES);
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runMigrations(JdbcDatabaseContainer<?> jdbcDatabaseContainer) throws LiquibaseException, SQLException {
        String url = jdbcDatabaseContainer.getJdbcUrl();
        String username = jdbcDatabaseContainer.getUsername();
        String password = jdbcDatabaseContainer.getPassword();
        try (JdbcConnection jdbcConnection = new JdbcConnection(
            DriverManager.getConnection(url, username, password))) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
            Liquibase liquibase = new Liquibase("migration/master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
    @Test
    @DisplayName("Test postgre container")
    public void testPostgreContainer() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build());
        jdbcTemplate.update("INSERT INTO Chat (tg_chat_id) VALUES (?)", 1L);
        long chatId = jdbcTemplate.queryForObject("SELECT tg_chat_id FROM Chat WHERE id = (?)", Long.class, 1L);

        assertThat(1L).isEqualTo(chatId);

        jdbcTemplate.update("INSERT INTO Link (link_name) VALUES (?)", "https://github.com/alexkorep04/Course");
        jdbcTemplate.update("INSERT INTO Link (link_name) VALUES (?)", "https://github.com/alexkorep04/Labarotories-OOP");
        jdbcTemplate.update("UPDATE Link SET link_name = (?) WHERE link_id = (?)", "https://github.com/alexkorep04/Fractal-Image-Application", 2L);
        String linkName1 = jdbcTemplate.queryForObject("SELECT link_name FROM Link WHERE link_id = (?)", String.class, 1L);
        String linkName2 = jdbcTemplate.queryForObject("SELECT link_name FROM Link WHERE link_id = (?)", String.class, 2L);

        assertThat("https://github.com/alexkorep04/Course").isEqualTo(linkName1);
        assertThat("https://github.com/alexkorep04/Fractal-Image-Application").isEqualTo(linkName2);

        jdbcTemplate.update("INSERT INTO Chat_link (chat_id, link_id) VALUES (?, ?)", 1L, 1L);
        jdbcTemplate.update("INSERT INTO Chat_link (chat_id, link_id) VALUES (?, ?)", 1L, 2L);
        long id = jdbcTemplate.queryForObject("SELECT chat_id FROM Chat_link WHERE chat_id = (?) AND link_id = (?)", Long.class, 1L, 2L);

        assertThat(1L).isEqualTo(id);
    }
}
