package com.interbank.antifraude.antifraudservice;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.interbank.antifraude.antifraudservice.application.services.impl.AntiFraudService;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain;
import com.interbank.antifraude.antifraudservice.infrastructure.kafka.TransactionEventConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
  
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
class TransactionEventConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventConsumer.class);

    @Mock
    private AntiFraudService antiFraudService;

    @InjectMocks
    private TransactionEventConsumer transactionEventConsumer;

    private Gson gson;
    private String validJsonEvent;
    private String jsonWithExtraQuotes;
    private String invalidJsonEvent;
    private EventTrxDomain eventTrxDomain;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        eventTrxDomain = new EventTrxDomain();
        eventTrxDomain.setTransactionExternalId("1234");
        eventTrxDomain.setTransactionStatus("pendiente");
        eventTrxDomain.setValue(500);

        validJsonEvent = gson.toJson(eventTrxDomain);
        jsonWithExtraQuotes = "\"" + validJsonEvent.replace("\"", "\\\"") + "\"";
        invalidJsonEvent = "{invalidJson";
    }

    @Test
    void testConsumeValidMessage() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("trx-topic", 0, 0, "key", validJsonEvent);

        doNothing().when(antiFraudService).consumeEvent(Mockito.any(EventTrxDomain.class));

        assertDoesNotThrow(() -> transactionEventConsumer.consume(record));

        verify(antiFraudService).consumeEvent(Mockito.any(EventTrxDomain.class));
    }

    @Test
    void testConsumeMessageWithExtraQuotes() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("trx-topic", 0, 0, "key", jsonWithExtraQuotes);

        doNothing().when(antiFraudService).consumeEvent(Mockito.any(EventTrxDomain.class));

        assertDoesNotThrow(() -> transactionEventConsumer.consume(record));

        verify(antiFraudService).consumeEvent(Mockito.any(EventTrxDomain.class));
    }

    @Test
    void testConsumeInvalidMessage() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("trx-topic", 0, 0, "key", invalidJsonEvent);

        assertDoesNotThrow(() -> transactionEventConsumer.consume(record));

        verify(antiFraudService, never()).consumeEvent(Mockito.any(EventTrxDomain.class));
    }

    @Test
    void testConsumeThrowsException() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("trx-topic", 0, 0, "key", validJsonEvent);

        doThrow(new RuntimeException("Unexpected error")).when(antiFraudService).consumeEvent(Mockito.any(EventTrxDomain.class));

        assertDoesNotThrow(() -> transactionEventConsumer.consume(record));

        verify(antiFraudService).consumeEvent(Mockito.any(EventTrxDomain.class));
    }

    @Test
    void testLogMessages() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("trx-topic", 0, 0, "key", validJsonEvent);

        assertDoesNotThrow(() -> transactionEventConsumer.consume(record));

        log.info("Mensaje recibido crudo desde Kafka: {}", record.value());
        log.info("Mensaje deserializado: {}", gson.fromJson(record.value(), EventTrxDomain.class));
    }

    @Test
    void testJsonSyntaxExceptionIsHandled() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("trx-topic", 0, 0, "key", invalidJsonEvent);

        assertDoesNotThrow(() -> transactionEventConsumer.consume(record));

        log.error("Error de sintaxis en JSON durante la deserialización");
    }
}
