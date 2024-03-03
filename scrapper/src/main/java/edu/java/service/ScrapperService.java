package edu.java.service;

import edu.java.dto.response.LinkResponse;
import java.net.URI;
import java.util.List;

public interface ScrapperService {
    void registerChat(long id);

    void deleteChat(long id);

    List<LinkResponse> getLinks(long id);

    LinkResponse addLink(long id, URI link);

    LinkResponse deleteLink(long id, URI link);
}
