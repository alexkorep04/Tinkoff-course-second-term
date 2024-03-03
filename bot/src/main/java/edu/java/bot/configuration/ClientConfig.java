package edu.java.bot.configuration;

import edu.java.bot.cllient.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public ScrapperClient getScrapperClient() {
        return new ScrapperClient();
    }
}
