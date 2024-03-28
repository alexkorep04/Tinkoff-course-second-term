package edu.java.bot.configuration;

import edu.java.bot.cllient.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfig {
    @Autowired
    private Retry retry;

    @Bean
    public ScrapperClient getScrapperClient() {
        return new ScrapperClient(retry);
    }
}
