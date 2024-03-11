package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.exception.ChatAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Start the bot";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the start command");
        if (update.message().text().split(" ").length != 1) {
            log.info(USER + update.message().chat().username() + " used invalid format of start command");
            return new SendMessage(update.message().chat().id(),
                "Invalid format! To start work with bot use /start");
        }
        log.info(USER + update.message().chat().username() + " successfully got response to start command");
        try {
            scrapperClient.registerChat(update.message().chat().id()).block();
            return new SendMessage(update.message().chat().id(),
                "Hi! I'm link tracker bot! You can use me to check changes on GitHub and StackOverflow websites!");
        } catch (ChatAlreadyExistsException e) {
            return new SendMessage(update.message().chat().id(),
                "Sorry, you are already registered!");
        } catch (Exception e) {
            return null;
        }
    }
}
