package com.interbank.antifraude.antifraudservice;

import com.google.gson.Gson;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain;
import com.interbank.antifraude.antifraudservice.infrastructure.kafka.KafkaEventPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class KafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaEventPublisher kafkaEventPublisher;

    private Gson gson;
    private EventTrxDomain eventTrxDomain;

    @Value("${spring.kafka.topic.transaction-result}")
    private String resultTopic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gson = new Gson();
        eventTrxDomain = new EventTrxDomain();
        eventTrxDomain.setTransactionExternalId("1234");
        eventTrxDomain.setTransactionStatus("pendiente");
        eventTrxDomain.setValue(500);

        // Configurar el valor de resultTopic manualmente
        ReflectionTestUtils.setField(kafkaEventPublisher, "resultTopic", "topic-trx-validation-result");
    }

    @Test
    void testPublishEvent() {
        String jsonEvent = gson.toJson(eventTrxDomain);

        kafkaEventPublisher.publishEvent(eventTrxDomain);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), messageCaptor.capture());

        assertEquals("topic-trx-validation-result", topicCaptor.getValue());
        assertEquals(jsonEvent, messageCaptor.getValue());
    }
}