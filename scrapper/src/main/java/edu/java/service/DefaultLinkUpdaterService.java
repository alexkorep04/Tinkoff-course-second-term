package edu.java.service;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import edu.java.scheduler.GitHubLinkUpdater;
import edu.java.scheduler.StackOverflowLinkUpdater;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultLinkUpdaterService implements LinkUpdaterService {
    private final LinkRepository linkRepository;
    private final GitHubLinkUpdater gitHubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;

    public DefaultLinkUpdaterService(
        @Qualifier("jooqLinkRepository") LinkRepository linkRepository,
        GitHubLinkUpdater gitHubLinkUpdater,
        StackOverflowLinkUpdater stackOverflowLinkUpdater
    ) {
        this.linkRepository = linkRepository;
        this.gitHubLinkUpdater = gitHubLinkUpdater;
        this.stackOverflowLinkUpdater = stackOverflowLinkUpdater;
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public int update() {
        List<Link> links = linkRepository.findOldestLinks(3);
        int amount = 0;
        for (Link link: links) {
            if ("GitHubLink".equals(link.getType())
                && gitHubLinkUpdater.supports(link.getName())) {
                amount += gitHubLinkUpdater.update(link);
            } else if ("StackOverflowLink".equals(link.getType())
                && stackOverflowLinkUpdater.supports(link.getName())) {
                amount += stackOverflowLinkUpdater.update(link);
            }
        }
        return amount;
    }
}
