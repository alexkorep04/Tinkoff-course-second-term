package edu.java.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    public void send(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send(applicationConfig.kafka().topicName(), update);
        } catch (Exception e) {
            log.warn("Error during sending message in kafka");
            throw new RuntimeException(e.getMessage());
        }
    }
}
