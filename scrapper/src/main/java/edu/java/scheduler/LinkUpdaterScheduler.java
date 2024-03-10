package edu.java.scheduler;

import edu.java.service.LinkUpdaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        log.info("Update...");
        linkUpdaterService.update();
    }
}

