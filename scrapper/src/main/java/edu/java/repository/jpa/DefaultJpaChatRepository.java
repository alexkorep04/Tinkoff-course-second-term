package edu.java.repository.jpa;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.exception.ChatAlreadyExistsException;
import edu.java.repository.ChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DefaultJpaChatRepository implements ChatRepository {
    private final JpaChatRepository jpaChatRepository;

    @Override
    @Transactional
    public void add(long id) {
        Optional<Chat> chat = findById(id);
        if (chat.isPresent()) {
            throw new ChatAlreadyExistsException("Chat is registered!");
        }
        jpaChatRepository.save(new Chat(id));
    }

    @Override
    @Transactional
    public int remove(long id) {
        Chat chat = findById(id).get();
        List<Link> links = chat.getLinks();
        if (links != null) {
            links.clear();
        }
        jpaChatRepository.deleteNotUsedLinks();
        return jpaChatRepository.deleteChatById(id);
    }

    @Override
    public Optional<Chat> findById(long id) {
        return jpaChatRepository.findById(id);
    }

    @Override
    public List<Chat> findAll() {
        return jpaChatRepository.findAll();
    }
}
