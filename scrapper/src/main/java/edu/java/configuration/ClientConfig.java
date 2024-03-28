package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.DefaultGitHubClient;
import edu.java.client.DefaultStackOverflowClient;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    @Autowired
    private final Retry retry;

    @Bean
    public GitHubClient getGitHubClient() {
        return new DefaultGitHubClient(retry);
    }

    @Bean
    public StackOverflowClient getStackOverflowClient() {
        return new DefaultStackOverflowClient(retry);
    }

    @Bean
    public BotClient getBotClient() {
        return new BotClient(retry);
    }
}
