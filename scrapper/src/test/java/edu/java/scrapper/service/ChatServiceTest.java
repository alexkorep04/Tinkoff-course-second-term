package edu.java.scrapper.service;

import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.NoResourceException;
import edu.java.repository.ChatRepository;
import edu.java.repository.DefaultChatRepository;
import edu.java.service.JdbcTgChatService;
import edu.java.service.TgChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class ChatServiceTest {
    @Test
    @DisplayName("Test add")
    public void testGoodAdd(){
        ChatRepository chatRepository = mock(DefaultChatRepository.class);
        TgChatService tgChatService = new JdbcTgChatService(chatRepository);
        tgChatService.register(1L);
        verify(chatRepository).add(1L);
    }
    @Test
    @DisplayName("Test bad add")
    public void testBadAdd(){
        ChatRepository chatRepository = mock(DefaultChatRepository.class);
        TgChatService tgChatService = new JdbcTgChatService(chatRepository);
        doThrow(new DataIntegrityViolationException("")).when(chatRepository).add(1L);
        try {
            tgChatService.register(1L);
        } catch (ChatAlreadyExistsException e){
            assertThat("Chat is already registered!").isEqualTo(e.getMessage());
        }
        verify(chatRepository).add(1L);
    }

    @Test
    @DisplayName("Test remove")
    public void testGoodRemove(){
        ChatRepository chatRepository = mock(DefaultChatRepository.class);
        TgChatService tgChatService = new JdbcTgChatService(chatRepository);
        when(chatRepository.remove(1L)).thenReturn(1);
        tgChatService.unregister(1L);
        verify(chatRepository).remove(1L);
    }
    @Test
    @DisplayName("Test bad remove")
    public void testBadRemove(){
        ChatRepository chatRepository = mock(DefaultChatRepository.class);
        TgChatService tgChatService = new JdbcTgChatService(chatRepository);
        when(chatRepository.remove(1L)).thenReturn(0);
        try {
            tgChatService.unregister(1L);
        } catch (NoResourceException e){
            assertThat("No such chat in database!").isEqualTo(e.getMessage());
        }
        verify(chatRepository).remove(1L);
    }
}
