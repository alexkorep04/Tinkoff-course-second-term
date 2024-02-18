package edu.java.bot.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import org.springframework.stereotype.Service;

//временное решение до введения нормальной БД
@Service
public class DefaultLinkService  {
    private DefaultLinkService() {
    }

    private static final Map<Long, Set<String>> DATABASE = new ConcurrentHashMap<>();

    public static Set<String> getAllLinksOfUser(long id) {
        if (!DATABASE.containsKey(id)) {
            DATABASE.put(id, new ConcurrentSkipListSet<>());
        }
        return DATABASE.get(id);
    }

    public static void addLink(long id, String link) {
        if (!DATABASE.containsKey(id)) {
            DATABASE.put(id, new ConcurrentSkipListSet<>());
        }
        DATABASE.get(id).add(link);
    }


    public static void deleteLink(long id, String link) {
        if (!DATABASE.containsKey(id)) {
            DATABASE.put(id, new ConcurrentSkipListSet<>());
        }
        DATABASE.get(id).remove(link);
    }
}
