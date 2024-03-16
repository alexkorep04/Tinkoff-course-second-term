package edu.java.service;

import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.NoResourceException;
import edu.java.repository.ChatRepository;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DefaultTgChatService implements TgChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public DefaultTgChatService(@Qualifier("jooqChatRepository") ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void register(long tgChatId) {
        try {
            chatRepository.add(tgChatId);
        } catch (DataIntegrityViolationException | IntegrityConstraintViolationException e) {
            throw new ChatAlreadyExistsException("Chat is already registered!");
        }
    }

    @Override
    public void unregister(long tgChatId) {
        int amount = chatRepository.remove(tgChatId);
        if (amount == 0) {
            throw new NoResourceException("No such chat in database!");
        }
    }
}
