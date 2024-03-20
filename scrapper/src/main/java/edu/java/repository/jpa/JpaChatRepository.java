package edu.java.repository.jpa;

import edu.java.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    int deleteChatById(long id);

    @Modifying
    @Query("DELETE FROM Link l WHERE l.id NOT IN (SELECT DISTINCT cl.id FROM Chat c JOIN c.links cl)")
    int deleteNotUsedLinks();
}
