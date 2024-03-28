package edu.app.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exception.NoResourceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.net.URI;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UntrackCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Retry retry = Retry.fixedDelay(2, Duration.ofSeconds(1));
        Command command = new UntrackCommand(new ScrapperClient(retry));
        assertThat(command.description()).isEqualTo("Stop tracking the website page");
        assertThat(command.command()).isEqualTo("/untrack");
    }

    @Test
    @DisplayName("Test valid handling /untrack command")
    void testHandleUnrackCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack https://github.com/alexkorep04/Course");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.deleteLink(1754872960L, new RemoveLinkRequest(URI.create("https://github.com/alexkorep04/Course")))).thenReturn(
            Mono.just(new LinkResponse(1L, URI.create("https://github.com/alexkorep04/Course"))));
        Command untrackCommand = new UntrackCommand(scrapperClient);
        SendMessage response = untrackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04/Course is not tracked now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /untrack command")
    void testHandleNotValidUntrackCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);

        Command untrackCommand = new UntrackCommand(scrapperClient);
        SendMessage response = untrackCommand.handle(update);

        assertThat("Invalid format! Please use /untrack <URI>!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /untrack command when link is not valid")
    void testHandleUntrackCommandWithNotValidLink() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack https://github.com/alexkorep04");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        Command untrackCommand = new UntrackCommand(scrapperClient);
        SendMessage response = untrackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04 is not supported now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /untrack command when no such link for user")
    void testHandleUntrackCommandWhenLinkExists() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack https://github.com/alexkorep04/Course");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.deleteLink(1754872960L, new RemoveLinkRequest(URI.create("https://github.com/alexkorep04/Course")))).thenThrow(new NoResourceException("No link"));
        Command untrackCommand = new UntrackCommand(scrapperClient);
        SendMessage response = untrackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04/Course was not tracked earlier!").isEqualTo(response.getParameters().get("text"));
    }
}
