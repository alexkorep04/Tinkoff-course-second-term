package edu.java.repository.jpa;

import edu.java.dto.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    @Modifying
    @Query(value = "insert into chat_link (chat_id, link_id) values (:chatId, :linkId)", nativeQuery = true)
    void addLinkToChatLink(@Param("chatId") long chatId, @Param("linkId") long linkId);


    @Modifying
    @Query(value = " SELECT * FROM link WHERE link_id IN (SELECT link_id FROM chat_link WHERE chat_id = :chatId)",
           nativeQuery = true)
    List<Link> findAllLinksByChatId(@Param("chatId") long chatId);

    @Modifying
    @Query(value = " SELECT DISTINCT l.* FROM link l JOIN chat_link c ON l.link_id = c.link_id "
        + "WHERE c.chat_id = :chatId AND l.link_name = :linkName", nativeQuery = true)
    List<Link> findByChatIdAndUrl(@Param("chatId") long chatId, @Param("linkName") String linkName);

    @Modifying
    @Query(value = "SELECT DISTINCT c.chat_id FROM chat_link c JOIN link l ON l.link_id = c.link_id "
        + "WHERE l.link_name = :name", nativeQuery = true)
    List<Long> findChatsByLink(@Param("name") String name);

    @Modifying
    @Query(value = "DELETE FROM chat_link WHERE chat_id = :chatId AND link_id = :linkId", nativeQuery = true)
    int deleteFromChatLink(@Param("chatId") long chatId, @Param("linkId") long linkId);

    @Modifying
    @Query(value = "SELECT * FROM link ORDER BY last_check LIMIT :amount", nativeQuery = true)
    List<Link> findTopByOrderByLastCheck(@Param("amount") int amount);

    Optional<Link> findByName(String name);

    int deleteLinkById(long linkId);

}
