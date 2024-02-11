package edu.java.bot.controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MessageProcessor implements UserMessageProcessor {
    private final List<Command> commands;

    public MessageProcessor(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (Command command: commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        log.info("User with name " + update.message().chat().username()
            + " successfully got response to unknown command");
        return new SendMessage(update.message().chat().id(), "Unknown command!");
    }
}
