package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.DefaultGitHubClient;
import edu.java.client.DefaultStackOverflowClient;
import edu.java.client.GitHubClient;
import edu.java.client.ScrapperClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient getGitHubClient() {
        return new DefaultGitHubClient();
    }

    @Bean
    public StackOverflowClient getStackOverflowClient() {
        return new DefaultStackOverflowClient();
    }

    @Bean
    public BotClient getBotClient() {
        return new BotClient();
    }

    @Bean
    public ScrapperClient getScrapperClient() {
        return new ScrapperClient();
    }
}
