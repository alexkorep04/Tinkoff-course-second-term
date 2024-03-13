package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StartCommand implements Command {
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
        return new SendMessage(update.message().chat().id(),
            "Hi! I'm link tracker bot! You can use me to check changes on GitHub and StackOverflow websites!");
    }
}
