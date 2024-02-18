package edu.java.bot.service;

import java.util.Set;

public interface LinkService {
    Set<String> getAllLinksOfUser(long id);

    void addLink(long id, String link);

    void deleteLink(long id, String link);
}
