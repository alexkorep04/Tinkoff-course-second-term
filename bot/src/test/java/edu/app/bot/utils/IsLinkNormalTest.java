package edu.app.bot.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.ValidLink.isLinkNormal;
import static org.assertj.core.api.Assertions.assertThat;

public class IsLinkNormalTest {
    @Test
    @DisplayName("Test git links")
    public void testGitLinks() {
        assertThat(isLinkNormal("https://github.com/alexkorep04/Tinkoff-course-second-term")).isTrue();
        assertThat(isLinkNormal("https://github.com//Tinkoff-course-second-term")).isFalse();
        assertThat(isLinkNormal("https://github.com/alexkorep04/")).isFalse();
        assertThat(isLinkNormal("https://github.com/")).isFalse();
    }

    @Test
    @DisplayName("Test stackoverflow links")
    public void testStackOverflowLinks() {
        assertThat(isLinkNormal("https://stackoverflow.com/questions/78141333/liquibase-spring-relation-xxx-databasechangeloglock-does-not-exist-when-spe")).isTrue();
        assertThat(isLinkNormal("https://stackoverflow.com/questions/78141333")).isFalse();
        assertThat(isLinkNormal("https://stackoverflow.com/questions/")).isFalse();
    }
}
