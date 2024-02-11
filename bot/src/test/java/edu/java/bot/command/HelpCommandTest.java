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

public class HelpCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new HelpCommand();

        assertThat(command.description()).isEqualTo("Show all commands");
        assertThat(command.command()).isEqualTo("/help");
    }

    @Test
    @DisplayName("Test valid handling /help command")
    void testHandleHelpCommand() {
        Command helpCommand = new HelpCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/help");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = helpCommand.handle(update);

        String expected = "/start - start bot\n" + "/track <link> - start tracking the link\n" + "/untrack <link> - stop tracking the link\n" + "/list - show all tracking links\n";

        assertThat(expected).isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /help command")
    void testHandleNotValidHelpCommand() {
        Command helpCommand = new HelpCommand();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/help bot");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = helpCommand.handle(update);

        assertThat("Invalid format! To see all commands use /help").isEqualTo(response.getParameters().get("text"));
    }
}
