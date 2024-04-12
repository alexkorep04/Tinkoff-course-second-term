package edu.app.bot.service;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.exception.UpdateAlreadyExistsException;
import edu.java.bot.service.BotService;
import edu.java.bot.service.DefaultBotService;
import java.net.URI;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BotServiceTest {
    @Test
    @DisplayName("Test bot service")
    public void testBotService() {
        BotService botService = new DefaultBotService();
        LinkUpdateRequest
            linkUpdateRequest = new LinkUpdateRequest(1L, URI.create("https://github.com/alexkorep04"), "Repo", List.of(1L, 2L, 3L));
        assertDoesNotThrow(() -> botService.addNewUpdate(linkUpdateRequest));
        assertThrows(UpdateAlreadyExistsException.class ,() -> botService.addNewUpdate(linkUpdateRequest));
    }
}
