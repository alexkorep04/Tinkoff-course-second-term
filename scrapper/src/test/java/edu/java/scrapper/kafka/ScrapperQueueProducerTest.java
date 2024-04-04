package edu.java.scrapper.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.kafka.ScrapperQueueProducer;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScrapperQueueProducerTest {
    private ApplicationConfig applicationConfig;
    private ApplicationConfig.Kafka kafka;
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private ScrapperQueueProducer scrapperQueueProducer;
    @Before
    public void init() {
        applicationConfig = mock(ApplicationConfig.class);
        kafka = mock(ApplicationConfig.Kafka.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        scrapperQueueProducer = new ScrapperQueueProducer(applicationConfig, kafkaTemplate);
    }
    @Test
    @DisplayName("Test sending messages from producer")
    public void testSending() {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest();
        when(applicationConfig.kafka()).thenReturn(kafka);
        when(kafka.topicName()).thenReturn("topic1");
        when(kafkaTemplate.send("topic1", linkUpdateRequest)).thenReturn(null);

        scrapperQueueProducer.send(linkUpdateRequest);

        verify(kafkaTemplate).send("topic1", linkUpdateRequest);
    }
}
