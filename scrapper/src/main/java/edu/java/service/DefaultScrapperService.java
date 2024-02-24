package edu.java.service;

import edu.java.dto.response.LinkResponse;
import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.LinkAlreadyExistsException;
import edu.java.exception.NoChatException;
import edu.java.exception.NoLinkException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service
public class DefaultScrapperService implements ScrapperService {
    private final Map<Long, List<LinkResponse>> database = new ConcurrentHashMap<>();
    private static final String CHAT = "Chat is already registered!";
    private static final String NO_CHAT = "No such chat in database!";
    private static final String LINK_IN_DB = "Link is already in database!";
    private static final String NO_LINK = "No such link in database!";

    @Override
    public void registerChat(long id) {
        if (database.containsKey(id)) {
            throw new ChatAlreadyExistsException(CHAT);
        }
        database.put(id, new CopyOnWriteArrayList<>());
    }

    @Override
    public void deleteChat(long id) {
        if (!database.containsKey(id)) {
            throw new NoChatException(NO_CHAT);
        }
        database.remove(id);
    }

    @Override
    public List<LinkResponse> getLinks(long id) {
        if (!database.containsKey(id)) {
            throw new NoChatException(NO_CHAT);
        }
        return database.get(id);
    }

    @Override
    public LinkResponse addLink(long id, URI link) {
        if (!database.containsKey(id)) {
            throw new NoChatException(NO_CHAT);
        }
        List<LinkResponse> linkResponses = database.get(id);
        for (LinkResponse linkResponse: linkResponses) {
            if (linkResponse.getUrl().getPath().equals(link.getPath())) {
                throw new LinkAlreadyExistsException(LINK_IN_DB);
            }
        }
        LinkResponse linkResponse = new LinkResponse((long) (linkResponses.size() + 1), link);
        linkResponses.add(linkResponse);
        return linkResponse;
    }

    @Override
    public LinkResponse deleteLink(long id, URI link) {
        if (!database.containsKey(id)) {
            throw new NoChatException(NO_CHAT);
        }
        List<LinkResponse> linkResponses = database.get(id);
        for (LinkResponse linkResponse: linkResponses) {
            if (linkResponse.getUrl().getPath().equals(link.getPath())) {
                linkResponses.remove(linkResponse);
                return linkResponse;
            }
        }
        throw new NoLinkException(NO_LINK);
    }
}
