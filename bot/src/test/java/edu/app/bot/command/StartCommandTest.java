package edu.app.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.cllient.ScrapperClient;
import edu.java.bot.command.Command;
import edu.java.bot.command.StartCommand;
import edu.java.bot.exception.ChatAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartCommandTest {
    @Test
    @DisplayName("Test name and description")
    public void testNameAndDescription() {
        Command command = new StartCommand(new ScrapperClient());

        assertThat(command.description()).isEqualTo("Start the bot");
        assertThat(command.command()).isEqualTo("/start");
    }

    @Test
    @DisplayName("Test valid handling /start command")
    void testHandleStartCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.registerChat(1754872960L)).thenReturn(Mono.just("Чат успешно добавлен"));
        Command startCommand = new StartCommand(scrapperClient);
        SendMessage response = startCommand.handle(update);

        assertThat("Hi! I'm link tracker bot! You can use me to check changes on GitHub and StackOverflow websites!").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test not valid handling /start command")
    void testHandleNotValidStartCommand() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Command startCommand = new StartCommand(scrapperClient);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start bot");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = startCommand.handle(update);

        assertThat("Invalid format! To start work with bot use /start").isEqualTo(response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Test handling /start command when chat exists")
    void testHandleStartCommandWhenChatExists() {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(1754872960L);
        when(scrapperClient.registerChat(1754872960L)).thenThrow(new ChatAlreadyExistsException("Chat is already exists"));
        Command startCommand = new StartCommand(scrapperClient);
        SendMessage response = startCommand.handle(update);

        assertThat("Sorry, you are already registered!").isEqualTo(response.getParameters().get("text"));
    }
}
