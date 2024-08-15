package com.interbank.transacciones.transactionservice;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;
import com.interbank.transacciones.transactionservice.domain.TransactionEventHandler;
import com.interbank.transacciones.transactionservice.infraestructure.kafka.KafkaTransactionResultConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KafkaTransactionResultConsumerTest {

    @Mock
    private TransactionEventHandler transactionEventHandler;

    @InjectMocks
    private KafkaTransactionResultConsumer kafkaTransactionResultConsumer;

    private final Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsume_Success() {
        // Arrange
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setTransactionExternalId("1234");
        transactionEvent.setTransactionStatus("SUCCESS");

        String json = gson.toJson(transactionEvent);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic-trx-validation-result", 0, 0L, null, json);

        // Act
        kafkaTransactionResultConsumer.consume(record);

        // Assert
        verify(transactionEventHandler, times(1)).handleTransactionEvent(any(TransactionEvent.class));
    }

    @Test
    void testConsume_JsonSyntaxException() {
        // Arrange
        String invalidJson = "invalid-json";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic-trx-validation-result", 0, 0L, null, invalidJson);

        // Act
        kafkaTransactionResultConsumer.consume(record);

        // Assert
        verify(transactionEventHandler, never()).handleTransactionEvent(any(TransactionEvent.class));
    }

    @Test
    void testConsume_UnexpectedException() {
        // Arrange
        String validJson = "{\"transactionExternalId\":\"1234\",\"transactionStatus\":\"SUCCESS\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic-trx-validation-result", 0, 0L, null, validJson);

        doThrow(new RuntimeException("Unexpected Error")).when(transactionEventHandler).handleTransactionEvent(any(TransactionEvent.class));

        // Act
        try {
            kafkaTransactionResultConsumer.consume(record);
        } catch (RuntimeException e) {
            // Expected exception, no action needed
        }

        // Assert
        verify(transactionEventHandler, times(1)).handleTransactionEvent(any(TransactionEvent.class));
    }

}
