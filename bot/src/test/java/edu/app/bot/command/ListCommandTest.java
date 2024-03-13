package edu.app.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.ListCommand;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListsLinkResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new ListCommand(new ScrapperClient());

        assertThat(command.description()).isEqualTo("Show all tracked links");
        assertThat(command.command()).isEqualTo("/list");
    }

    @Test
    @DisplayName("Test valid handling /list command")
    void testHandleListCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.getLinks(1754872960L)).thenReturn(
            Mono.just(new ListsLinkResponse(List.of(new LinkResponse(1L, URI.create("https://github.com/alexkorep04/Course"))), 1)));
        Command listCommand = new ListCommand(scrapperClient);
        SendMessage response = listCommand.handle(update);

        assertThat("List of tracked links:\nhttps://github.com/alexkorep04/Course\n").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /list command")
    void testHandleNotValidListCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list bot");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        Command listCommand = new ListCommand(scrapperClient);
        SendMessage response = listCommand.handle(update);

        assertThat("Invalid format! To see all tracked links use /list").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test valid handling /list command when no links")
    void testHandleListCommandWhenNoLinks() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.getLinks(1754872960L)).thenReturn(
            Mono.just(new ListsLinkResponse(new ArrayList<>(), 0)));
        Command listCommand = new ListCommand(scrapperClient);
        SendMessage response = listCommand.handle(update);

        assertThat("No tracked links!").isEqualTo(response.getParameters().get("text"));
    }
}
