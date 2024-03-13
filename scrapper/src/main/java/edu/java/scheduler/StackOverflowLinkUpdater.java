package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.StackOverflowClient;
import edu.java.dto.Link;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.entity.StackOverflowResponse;
import edu.java.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkUpdater implements LinkUpdater {
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public boolean supports(String url) {
        return url.startsWith("https://stackoverflow.com/questions/");
    }

    @Override
    public int update(Link link) {
        String url = link.getName();
        String[] parts = url.split("/");
        int question = Integer.parseInt(parts[parts.length - 1]);
        StackOverflowResponse stackOverflowResponse = stackOverflowClient.fetchQuestion(question).block();
        linkRepository.updateLastCheck(OffsetDateTime.now(ZoneId.of("UTC")), url);
        if (OffsetDateTime.MIN.equals(link.getLastUpdate())) {
            linkRepository.updateLastUpdate(stackOverflowResponse.getItems().getFirst().getLastActivityDate(), url);
        } else if (stackOverflowResponse.getItems().getFirst().getLastActivityDate().isAfter(link.getLastUpdate())) {
            linkRepository.updateLastUpdate(stackOverflowResponse.getItems().getFirst().getLastActivityDate(), url);
            LinkUpdateRequest
                linkUpdateRequest = new LinkUpdateRequest(link.getId(), URI.create(url),
                "Update on link " + url, linkRepository.findChatsByLink(url));
            botClient.sendUpdate(linkUpdateRequest).block();
            return 1;
        }
        return 0;
    }
}
