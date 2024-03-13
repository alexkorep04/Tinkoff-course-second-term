package edu.app.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exception.LinkAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrackCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new TrackCommand(new ScrapperClient());

        assertThat(command.description()).isEqualTo("Start tracking the website page");
        assertThat(command.command()).isEqualTo("/track");
    }

    @Test
    @DisplayName("Test valid handling /track command")
    void testHandleTrackCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track https://github.com/alexkorep04/Course");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.addLink(1754872960L, new AddLinkRequest(URI.create("https://github.com/alexkorep04/Course")))).thenReturn(
            Mono.just(new LinkResponse(1L, URI.create("https://github.com/alexkorep04/Course"))));
        Command trackCommand = new TrackCommand(scrapperClient);
        SendMessage response = trackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04/Course is tracking now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /track command")
    void testHandleNotValidTrackCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);

        Command trackCommand = new TrackCommand(scrapperClient);
        SendMessage response = trackCommand.handle(update);

        assertThat("Invalid format! Please use /track <URI>").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /track command when link is not valid")
    void testHandleTrackCommandWithNotValidLink() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track https://github.com/alexkorep04");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        Command trackCommand = new TrackCommand(scrapperClient);
        SendMessage response = trackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04 is not supported now!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /track command when link alrerady exists")
    void testHandleTrackCommandWhenLinkExists() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track https://github.com/alexkorep04/Course");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.addLink(1754872960L, new AddLinkRequest(URI.create("https://github.com/alexkorep04/Course")))).thenThrow(new LinkAlreadyExistsException("Link exists"));
        Command trackCommand = new TrackCommand(scrapperClient);
        SendMessage response = trackCommand.handle(update);

        assertThat("Link https://github.com/alexkorep04/Course was already tracked earlier!").isEqualTo(response.getParameters().get("text"));
    }
}
