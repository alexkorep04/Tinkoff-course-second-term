package edu.java.service;

import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.NoResourceException;
import edu.java.repository.ChatRepository;
import java.util.NoSuchElementException;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DefaultTgChatService implements TgChatService {
    private final ChatRepository chatRepository;
    private final static String NO_CHAT = "No such chat in database!";

    @Autowired
    public DefaultTgChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void register(long tgChatId) {
        try {
            chatRepository.add(tgChatId);
        } catch (DataIntegrityViolationException | ChatAlreadyExistsException
                 | IntegrityConstraintViolationException e) {
            throw new ChatAlreadyExistsException("Chat is already registered!");
        }
    }

    @Override
    public void unregister(long tgChatId) {
        try {
            int amount = chatRepository.remove(tgChatId);
            if (amount == 0) {
                throw new NoResourceException(NO_CHAT);
            }
        } catch (NoSuchElementException e) {
            throw new NoResourceException(NO_CHAT);
        }
    }
}
