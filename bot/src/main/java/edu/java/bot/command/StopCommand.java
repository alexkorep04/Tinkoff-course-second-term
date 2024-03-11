package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class StopCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/stop";
    }

    @Override
    public String description() {
        return "Stop tracking all links and delete chat";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the stop command");
        if (update.message().text().split(" ").length != 1) {
            log.info(USER + update.message().chat().username() + " used invalid format of stop command");
            return new SendMessage(update.message().chat().id(),
                "Invalid format! To stop work with bot use /stop");
        }
        log.info(USER + update.message().chat().username() + " successfully got response to stop command");
        try {
            scrapperClient.deleteChat(update.message().chat().id()).block();
            return new SendMessage(update.message().chat().id(),
                "Thank you for using Link Tracker bot! If you want to start work with bot again use /start");
        } catch (Exception e) {
            return null;
        }
    }
}
