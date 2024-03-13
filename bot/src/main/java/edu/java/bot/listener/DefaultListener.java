package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import edu.java.bot.controller.MessageProcessor;
import edu.java.bot.requestexecutor.Executor;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultListener implements Listener {
    private final MessageProcessor messageProcessor;
    private final Executor executor;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(
            update -> { Optional.ofNullable(messageProcessor.process(update))
                    .ifPresent(sendMessage -> executor.execute(sendMessage.parseMode(ParseMode.Markdown)));
            }
        );
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
