package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.dto.Link;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.entity.GitHubResponse;
import edu.java.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubLinkUpdater implements LinkUpdater {
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final GitHubClient gitHubClient;

    @Override
    public boolean supports(String url) {
        return url.startsWith("https://github.com/");
    }

    @Override
    public int update(Link link) {
        String url = link.getName();
        String[] parts = url.split("/");
        String account = parts[parts.length - 2];
        String repository = parts[parts.length - 1];
        GitHubResponse gitHubResponse;
        try {
            gitHubResponse = gitHubClient.fetchRepository(account, repository).block();
        } catch (Exception e) {
            return 0;
        }
        linkRepository.updateLastCheck(OffsetDateTime.now(ZoneId.of("UTC")), url);
        if (OffsetDateTime.MIN.equals(link.getLastUpdate())) {
            linkRepository.updateLastUpdate(gitHubResponse.getUpdateTime(), url);
        } else if (gitHubResponse.getUpdateTime().isAfter(link.getLastUpdate())) {
            linkRepository.updateLastUpdate(gitHubResponse.getUpdateTime(), url);
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(link.getId(), URI.create(url),
                    "Update on link " + url, linkRepository.findChatsByLink(url));
            botClient.sendUpdate(linkUpdateRequest).block();
            return 1;
        }
        return 0;
    }
}
