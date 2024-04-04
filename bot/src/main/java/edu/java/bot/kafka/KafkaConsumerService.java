package edu.java.bot.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.service.SendUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ApplicationConfig applicationConfig;
    private final SendUpdateService sendUpdateService;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @KafkaListener(topics = "${app.kafka.topicName}", groupId = "${app.kafka.consumer.group-id}")
    public void listen(LinkUpdateRequest update) {
        try {
            log.info("Update in kafka: " + update);
            sendUpdateService.sendUpdateToUser(update);
        } catch (Exception e) {
            log.warn("Exception in kafka, moved to error queue: " + update);
            sendMessageToErrorQueue(update);
        }
    }

    private void sendMessageToErrorQueue(LinkUpdateRequest update) {
        kafkaTemplate.send(applicationConfig.kafka().errorTopicName(), update);
    }
}
