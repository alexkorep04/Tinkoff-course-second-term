package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ListCommand implements Command {

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Show all tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the list command");
        if (update.message().text().split(" ").length != 1) {
            return new SendMessage(update.message().chat().id(),
                "Invalid format! To see all tracked links use /list");
        }
        log.info(USER + update.message().chat().username() + " does not have tracker links");
        return new SendMessage(update.message().chat().id(),
                "No tracked links! Bot is not able to support this operation now without DB!");
    }
}
