package edu.java.repository;

import edu.java.dto.ChatLink;
import java.util.List;

public interface ChatLinkRepository {
    List<ChatLink> findAll();

    List<ChatLink> findById(long chatId);
}
