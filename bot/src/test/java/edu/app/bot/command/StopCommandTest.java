package edu.app.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.StopCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StopCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Retry retry = Retry.fixedDelay(2, Duration.ofSeconds(1));
        Command command = new StopCommand(new ScrapperClient(retry));
        assertThat(command.description()).isEqualTo("Stop tracking all links and delete chat");
        assertThat(command.command()).isEqualTo("/stop");
    }

    @Test
    @DisplayName("Test valid handling /stop command")
    void testHandleStopCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/stop");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.deleteChat(1754872960L)).thenReturn(Mono.just("Чат успешно удален"));
        Command stopCommand = new StopCommand(scrapperClient);
        SendMessage response = stopCommand.handle(update);

        assertThat("Thank you for using Link Tracker bot! If you want to start work with bot again use /start").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /stop command")
    void testHandleNotValidStopCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Command stopCommand = new StopCommand(scrapperClient);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/stop bot");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = stopCommand.handle(update);

        assertThat("Invalid format! To stop work with bot use /stop").isEqualTo(response.getParameters().get("text"));
    }
}
