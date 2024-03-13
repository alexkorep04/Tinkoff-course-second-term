package edu.java.bot.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.ValidLink.isLinkNormal;
import static org.assertj.core.api.Assertions.*;

public class LinkTest {
    @Test
    @DisplayName("Test valid link")
    public void testValidLink() {
        boolean response1 = isLinkNormal("https://github.com/alexkorep04/");
        boolean response2 = isLinkNormal("https://stackoverflow.com/search?q=unsupported%20link");

        assertThat(response1).isTrue();
        assertThat(response2).isTrue();
    }
    @Test
    @DisplayName("Test not valid link")
    public void testNotValidLink() {
        boolean response = isLinkNormal("https://vk.com/");

        assertThat(response).isFalse();
    }
}
