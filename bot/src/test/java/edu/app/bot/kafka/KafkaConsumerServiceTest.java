package edu.app.bot.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.kafka.KafkaConsumerService;
import edu.java.bot.service.SendUpdateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaConsumerServiceTest {
    private SendUpdateService sendUpdateService;
    private ApplicationConfig applicationConfig;
    private ApplicationConfig.Kafka kafka;
    private KafkaTemplate<String ,LinkUpdateRequest> kafkaTemplate;
    private LinkUpdateRequest linkUpdateRequest;
    @Before
    public void init() {
         sendUpdateService = mock(SendUpdateService.class);
         kafkaTemplate = mock(KafkaTemplate.class);
         kafka = mock(ApplicationConfig.Kafka.class);
         applicationConfig = mock(ApplicationConfig.class);
         linkUpdateRequest = new LinkUpdateRequest();
    }
    @Test
    @DisplayName("Test consumer service")
    public void testConsumer() {
        KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(applicationConfig, sendUpdateService, kafkaTemplate);
        kafkaConsumerService.listen(linkUpdateRequest);

        verify(sendUpdateService).sendUpdateToUser(linkUpdateRequest);
    }
}
