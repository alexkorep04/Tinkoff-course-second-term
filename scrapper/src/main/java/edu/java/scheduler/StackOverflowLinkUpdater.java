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
import org.springframework.stereotype.Service;

@Service
public class StackOverflowLinkUpdater implements LinkUpdater {
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final StackOverflowClient stackOverflowClient;

    public StackOverflowLinkUpdater(
        BotClient botClient,
        LinkRepository linkRepository,
        StackOverflowClient stackOverflowClient
    ) {
        this.botClient = botClient;
        this.linkRepository = linkRepository;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public boolean supports(String url) {
        return url.startsWith("https://stackoverflow.com/questions/");
    }

    @Override
    public int update(Link link) {
        String url = link.getName();
        String[] parts = url.split("/");
        StackOverflowResponse stackOverflowResponse;
        int question;
        try {
            question = Integer.parseInt(parts[parts.length - 2]);
            stackOverflowResponse = stackOverflowClient.fetchQuestion(question).block();
        } catch (Exception e) {
            return 0;
        }
        linkRepository.updateLastCheck(OffsetDateTime.now(ZoneId.of("UTC")), url);
        if (link.getLastUpdate() == null) {
            linkRepository.updateLastUpdate(stackOverflowResponse.getItems().getFirst().getLastActivityDate(), url);
        } else if (stackOverflowResponse.getItems().getFirst().getLastActivityDate().isAfter(link.getLastUpdate())) {
            linkRepository.updateLastUpdate(stackOverflowResponse.getItems().getFirst().getLastActivityDate(), url);
            LinkUpdateRequest
                linkUpdateRequest = new LinkUpdateRequest(link.getId(), URI.create(url),
                "Update on StackOverflow question  " + question + "\n" + url, linkRepository.findChatsByLink(url));
            botClient.sendUpdate(linkUpdateRequest).block();
            return 1;
        }
        return 0;
    }
}
