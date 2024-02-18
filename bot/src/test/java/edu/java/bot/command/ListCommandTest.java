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

public class ListCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new ListCommand();

        assertThat(command.description()).isEqualTo("Show all tracked links");
        assertThat(command.command()).isEqualTo("/list");
    }

    @Test
    @DisplayName("Test valid handling /list command")
    void testHandleStartCommand() {
        Command listCommand  = new ListCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = listCommand.handle(update);

        assertThat("No tracked links!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /list command")
    void testHandleNotValidStartCommand() {
        Command listCommand = new ListCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list bot");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = listCommand.handle(update);

        assertThat("Invalid format! To see all tracked links use /list").isEqualTo(response.getParameters().get("text"));
    }
}
