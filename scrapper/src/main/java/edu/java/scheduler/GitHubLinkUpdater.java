package edu.java.scheduler;

import edu.java.client.GitHubClient;
import edu.java.dto.Link;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.entity.GitHubResponse;
import edu.java.repository.LinkRepository;
import edu.java.service.SendService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Service;

@Service
public class GitHubLinkUpdater implements LinkUpdater {
    private final SendService sendService;
    private final LinkRepository linkRepository;
    private final GitHubClient gitHubClient;
    private final static String IN = " in ";

    public GitHubLinkUpdater(SendService sendService,
        LinkRepository linkRepository, GitHubClient gitHubClient) {
        this.sendService = sendService;
        this.linkRepository = linkRepository;
        this.gitHubClient = gitHubClient;
    }

    @Override
    public boolean supports(String url) {
        return url.startsWith("https://github.com/");
    }

    @Override
    public int update(Link link) {
        int amount = 0;
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
        amount += checkIssuesAmount(account, repository, link, gitHubResponse);
        if (amount == 0) {
            amount += checkLastCommit(account, repository, link, gitHubResponse, false);
        } else {
            amount += checkLastCommit(account, repository, link, gitHubResponse, true);
        }
        amount += checkLastUpdate(account, repository, link, gitHubResponse);
        return amount;
    }

    private int checkLastUpdate(String account, String repository, Link link, GitHubResponse gitHubResponse) {
        if (link.getLastUpdate() == null) {
            linkRepository.updateLastUpdate(gitHubResponse.getUpdateTime(), link.getName());
        } else if (gitHubResponse.getUpdateTime().isAfter(link.getLastUpdate())) {
            linkRepository.updateLastUpdate(gitHubResponse.getUpdateTime(), link.getName());
            String description = "Something new at the GitHub repository of "
                + account + IN + repository + "\n"
                + link.getName();
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(link.getId(), URI.create(link.getName()),
                    description, linkRepository.findChatsByLink(link.getName()));
            sendService.update(linkUpdateRequest);
            return 1;
        }
        return 0;
    }

    private int checkLastCommit(String account, String repository, Link link, GitHubResponse gitHubResponse,
        boolean wasNewIssue) {
        if (link.getLastUpdate() == null) {
            linkRepository.updateLastCommit(gitHubResponse.getPushedTime(), link.getName());
        } else if (gitHubResponse.getPushedTime().isAfter(link.getLastCommit())) {
            linkRepository.updateLastCommit(gitHubResponse.getPushedTime(), link.getName());
            if (!wasNewIssue) {
                String description = "New commit at the GitHub repository of "
                    + account + IN + repository + "\n"
                    + link.getName();
                LinkUpdateRequest linkUpdateRequest =
                    new LinkUpdateRequest(link.getId(), URI.create(link.getName()),
                        description, linkRepository.findChatsByLink(link.getName()));
                sendService.update(linkUpdateRequest);
                return 1;
            }
        }
        return 0;
    }

    private int checkIssuesAmount(String account, String repository, Link link, GitHubResponse gitHubResponse) {
        if (link.getAmountOfIssues() == -1 || gitHubResponse.getIssuesCount() < link.getAmountOfIssues()) {
            linkRepository.updateAmountOfIssues(gitHubResponse.getIssuesCount(), link.getName());
        } else if (gitHubResponse.getIssuesCount() > link.getAmountOfIssues()) {
            linkRepository.updateAmountOfIssues(gitHubResponse.getIssuesCount(), link.getName());
            String description = "New issue at the GitHub repository of "
                + account + IN + repository + "\n"
                + link.getName();
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(link.getId(), URI.create(link.getName()),
                    description, linkRepository.findChatsByLink(link.getName()));
            sendService.update(linkUpdateRequest);
            return 1;
        }
        return 0;
    }
}
