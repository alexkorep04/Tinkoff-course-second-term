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

public class UntrackCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new UntrackCommand();

        assertThat(command.description()).isEqualTo("Stop tracking the website page");
        assertThat(command.command()).isEqualTo("/untrack");
    }

    @Test
    @DisplayName("Test valid handling /untrack command with correct link")
    void testHandleUntrackCommand() {
        Command untrackCommand = new UntrackCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack https://github.com/alexkorep04");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = untrackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04 is not tracked now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /untrack command with correct link")
    void testHandleUntrackCommandWWithNotCorrectLink() {
        Command untrackCommand = new UntrackCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track https://vk.com");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = untrackCommand.handle(update);

        assertThat("Link https://vk.com is not supported now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /untrack command")
    void testHandleNotValidUntrackCommand() {
        Command untrackCommand = new UntrackCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = untrackCommand.handle(update);

        assertThat("Invalid format! Please use /untrack <URI>!").isEqualTo(response.getParameters().get("text"));
    }
}
