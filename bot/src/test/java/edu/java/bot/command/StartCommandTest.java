package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new StartCommand();

        assertThat(command.description()).isEqualTo("Start the bot");
        assertThat(command.command()).isEqualTo("/start");
    }

    @Test
    @DisplayName("Test valid handling /start command")
    void testHandleStartCommand() {
        Command startCommand = new StartCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = startCommand.handle(update);

        assertThat("Hi! I'm link tracker bot! You can use me to check changes on GitHub and StackOverflow websites!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /start command")
    void testHandleNotValidStartCommand() {
        Command startCommand = new StartCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start bot");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = startCommand.handle(update);

        assertThat("Invalid format! To start work with bot use /start").isEqualTo(response.getParameters().get("text"));
    }
}
