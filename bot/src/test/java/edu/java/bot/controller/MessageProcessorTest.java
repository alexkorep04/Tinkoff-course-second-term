package edu.java.bot.controller;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
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
            new ListCommand(),
            new UntrackCommand(),
            new TrackCommand(),
            new StartCommand(),
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
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = messageProcessor.process(update);

        assertThat("Hi! I'm link tracker bot! You can use me to check changes on GitHub and StackOverflow websites!").isEqualTo(response.getParameters().get("text"));
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

        assertThat("Unknown command!").isEqualTo(response.getParameters().get("text"));
    }
}
