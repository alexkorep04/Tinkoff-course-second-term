package edu.java.bot.service;

import edu.java.bot.bot.DefaultBot;
import edu.java.bot.dto.request.LinkUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SendUpdateService {
    private final DefaultBot bot;

    public void sendUpdateToUser(LinkUpdateRequest linkUpdateRequest) {
        List<Long> chats = linkUpdateRequest.getTgChatIds();
        log.info(chats.toString());
        for (Long chatId: chats) {
            bot.send(chatId, linkUpdateRequest.getDescription());
        }
    }
}
