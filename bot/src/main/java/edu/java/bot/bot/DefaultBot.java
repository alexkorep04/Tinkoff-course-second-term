package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.executor.Executor;
import edu.java.bot.listener.Listener;
import edu.java.bot.processor.MessageProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DefaultBot implements Bot {
    private final TelegramBot telegramBot;
    private final Listener listener;
    private final MessageProcessor processor;
    private final Executor executor;

    @Override
    @PostConstruct
    public void start() {
        log.info("Start link tracker bot");
        SetMyCommands setMyCommands = new SetMyCommands(
            processor.commands().stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        );
        executor.execute(setMyCommands);
        telegramBot.setUpdatesListener(listener);
    }

    @Override
    public void close() {
        telegramBot.shutdown();
        log.info("Stop link tracker bot");
    }

    public void send(long chatId, String description) {
        telegramBot.execute(new SendMessage(chatId, description));
        log.info(chatId);
    }
}
