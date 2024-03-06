package edu.java.service;

import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.NoResourceException;
import edu.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        try {
            chatRepository.add(tgChatId);
        } catch (DataIntegrityViolationException e) {
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
