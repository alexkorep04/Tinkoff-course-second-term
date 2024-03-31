package edu.java.service;

import edu.java.client.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.kafka.ScrapperQueueProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendService {
    private final ApplicationConfig applicationConfig;
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final BotClient botClient;

    public void update(LinkUpdateRequest linkUpdateRequest) {
        if (applicationConfig.useQueue()) {
            scrapperQueueProducer.send(linkUpdateRequest);
        } else {
            botClient.sendUpdate(linkUpdateRequest).block();
        }
    }
}
