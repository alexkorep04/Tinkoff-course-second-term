package edu.app.bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.processor.MessageProcessor;
import edu.java.bot.processor.UserMessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageProcessorTest {

    List<Command> commands;
    @BeforeEach
    public void init() {
        commands = List.of(
            new HelpCommand()
        );
    }

    @Test
    @DisplayName("Test commands method")
    public void testCommandsMethod() {
        UserMessageProcessor messageProcessor = new MessageProcessor(commands);

        assertThat(commands).isEqualTo(messageProcessor.commands());
    }

    @Test
    @DisplayName("Test processing to StartCommand handler")
    void testProcessingToStartCommand() {
        MessageProcessor messageProcessor = new MessageProcessor(commands);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/help");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = messageProcessor.process(update);

        assertThat("/start - start bot\n"
                + "/track <link> - start tracking the link\n"
               + "/untrack <link> - stop tracking the link\n"
               + "/list - show all tracking links\n"
            ).isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test unknown command response")
    void testUnknownCommand() {
        MessageProcessor messageProcessor = new MessageProcessor(commands);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/hello");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = messageProcessor.process(update);

        assertThat("Unknown command! Use /help to see all commands!").isEqualTo(response.getParameters().get("text"));
    }
}
