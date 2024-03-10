package edu.java.bot.controller;


import edu.java.bot.bot.Bot;
import edu.java.bot.bot.DefaultBot;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.dto.request.LinkUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BotController {
    private final DefaultBot bot;
    private static final String UPDATE_PROCESSED = "Обновление обработано";

    @PostMapping("/updates")
    public String doUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        List<Long> chats = linkUpdateRequest.getTgChatIds();
        for (Long chatId: chats) {
            bot.send(chatId, linkUpdateRequest.getDescription());
        }
        return UPDATE_PROCESSED;
    }
}
