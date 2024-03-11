package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class HelpCommand implements Command {

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Show all commands";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info(USER + update.message().chat().username() + " turned the help command");
        if (update.message().text().split(" ").length != 1) {
            return new SendMessage(update.message().chat().id(),
                "Invalid format! To see all commands use /help");
        }
        StringBuilder commands = new StringBuilder();
        commands.append("/start - start bot\n")
            .append("/track <link> - start tracking the link\n")
            .append("/untrack <link> - stop tracking the link\n")
            .append("/list - show all tracking links\n");
        log.info(USER + update.message().chat().username() + " successfully got response to help command");
        return new SendMessage(update.message().chat().id(),
            commands.toString());
    }
}
