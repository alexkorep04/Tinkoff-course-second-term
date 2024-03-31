package edu.java.scrapper.service;

import edu.java.client.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.request.LinkUpdateRequest;
import edu.java.kafka.ScrapperQueueProducer;
import edu.java.service.SendService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendServiceTest {
    private ApplicationConfig applicationConfig;
    private ScrapperQueueProducer scrapperQueueProducer;
    private BotClient botClient;
    private LinkUpdateRequest linkUpdateRequest;
    @Before
    public void init() {
        applicationConfig = mock(ApplicationConfig.class);
        scrapperQueueProducer = mock(ScrapperQueueProducer.class);
        botClient = mock(BotClient.class);
        linkUpdateRequest = new LinkUpdateRequest();
    }
    @Test
    @DisplayName("Test kafka send")
    public void testKafkaSend() {
        when(applicationConfig.useQueue()).thenReturn(true);
        SendService sendService = new SendService(applicationConfig, scrapperQueueProducer, botClient);

        sendService.update(linkUpdateRequest);

        verify(scrapperQueueProducer).send(linkUpdateRequest);
    }

    @Test
    @DisplayName("Test http send")
    public void testHttpSend() {
        when(applicationConfig.useQueue()).thenReturn(false);
        when(botClient.sendUpdate(linkUpdateRequest)).thenReturn(Mono.just("Обновление обработано"));
        SendService sendService = new SendService(applicationConfig, scrapperQueueProducer, botClient);

        sendService.update(linkUpdateRequest);

        verify(botClient).sendUpdate(linkUpdateRequest);
    }
}
