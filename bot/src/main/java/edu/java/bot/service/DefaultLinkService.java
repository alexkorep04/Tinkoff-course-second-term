package edu.java.bot.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import org.springframework.stereotype.Service;

@Service
public class DefaultLinkService {
    private DefaultLinkService() {
    }


    private static final Map<Long, Set<String>> DATABASE = new ConcurrentHashMap<>();

    public static Set<String> getAllLinksOfUser(long id) {
        if (!DATABASE.containsKey(id)) {
            DATABASE.put(id, new ConcurrentSkipListSet<>());
        }
        return DATABASE.get(id);
    }

    public static boolean addLink(long id, String link) {
        if (!DATABASE.containsKey(id)) {
            DATABASE.put(id, new ConcurrentSkipListSet<>());
        }
        if (DATABASE.get(id).contains(link)) {
            return false;
        }
        DATABASE.get(id).add(link);
        return true;
    }


    public static boolean deleteLink(long id, String link) {
        if (!DATABASE.containsKey(id)) {
            DATABASE.put(id, new ConcurrentSkipListSet<>());
        }
        if (!DATABASE.get(id).contains(link)) {
            return false;
        }
        DATABASE.get(id).remove(link);
        return true;
    }
}
