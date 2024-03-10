package edu.java.scheduler;

import edu.java.dto.Link;

public interface LinkUpdater {
    boolean supports(String url);

    int update(Link link);
}
