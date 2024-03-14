package edu.java.configuration;

import edu.java.scheduler.LinkUpdaterScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerConfig {
    @Bean
    public LinkUpdaterScheduler getScheduler() {
        return new LinkUpdaterScheduler();
    }
}
