package edu.java.scrapper.service;

import edu.java.dto.request.LinkUpdateRequest;
import edu.java.exception.UpdateAlreadyExistsException;
import edu.java.service.BotService;
import edu.java.service.DefaultBotService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import java.net.URI;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BotServiceTest {
    @Test
    @DisplayName("Test bot service")
    public void testBotService() {
        BotService botService = new DefaultBotService();
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(1L, URI.create("https://github.com/alexkorep04"), "Repo", List.of(1L, 2L, 3L));
        assertDoesNotThrow(() -> botService.addNewUpdate(linkUpdateRequest));
        assertThrows(UpdateAlreadyExistsException.class ,() -> botService.addNewUpdate(linkUpdateRequest));
    }
}
