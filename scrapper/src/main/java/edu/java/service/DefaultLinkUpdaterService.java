package edu.java.service;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import edu.java.scheduler.GitHubLinkUpdater;
import edu.java.scheduler.StackOverflowLinkUpdater;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultLinkUpdaterService implements LinkUpdaterService {
    private final LinkRepository linkRepository;
    private final GitHubLinkUpdater gitHubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;

    @SuppressWarnings("MagicNumber")
    @Override
    public int update() {
        List<Link> links = linkRepository.findOldestLinks(3);
        int amount = 0;
        for (Link link: links) {
            String[] parts = link.getName().split("//");
            if (parts[1].startsWith("github.com")
                && gitHubLinkUpdater.supports(link.getName())) {
                amount += gitHubLinkUpdater.update(link);
            } else if (parts[1].startsWith("stackoverflow.com")
                && stackOverflowLinkUpdater.supports(link.getName())) {
                amount += stackOverflowLinkUpdater.update(link);
            }
        }
        return amount;
    }
}
