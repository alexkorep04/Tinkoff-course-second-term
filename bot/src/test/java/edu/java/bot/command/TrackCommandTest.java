package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrackCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new TrackCommand();

        assertThat(command.description()).isEqualTo("Start tracking the website page");
        assertThat(command.command()).isEqualTo("/track");
    }

    @Test
    @DisplayName("Test valid handling /track command with correct link")
    void testHandleTrackCommand() {
        Command trackCommand = new TrackCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track https://github.com/alexkorep04");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = trackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04 is tracking!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /track command with correct link")
    void testHandleTrackCommandWWithNotCorrectLink() {
        Command trackCommand = new TrackCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track https://vk.com");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = trackCommand.handle(update);

        assertThat("Link https://vk.com is not supported now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /track command")
    void testHandleNotValidTrackCommand() {
        Command trackCommand = new TrackCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = trackCommand.handle(update);

        assertThat("Invalid format! Please use /track <URI>").isEqualTo(response.getParameters().get("text"));
    }
}
