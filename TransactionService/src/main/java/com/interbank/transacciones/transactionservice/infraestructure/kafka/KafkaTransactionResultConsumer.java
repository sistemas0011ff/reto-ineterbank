package com.interbank.transacciones.transactionservice.infraestructure.kafka;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;
import com.interbank.transacciones.transactionservice.domain.TransactionEventHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaTransactionResultConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaTransactionResultConsumer.class);
    private final Gson gson = new Gson();
    private final TransactionEventHandler transactionEventHandler;

    @Autowired
    public KafkaTransactionResultConsumer(TransactionEventHandler transactionEventHandler) {
        this.transactionEventHandler = transactionEventHandler;
    }

    @KafkaListener(topics = "topic-trx-validation-result", groupId = "transaction-service-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Mensaje recibido crudo desde Kafka: {}", record.value());
        try {
            String json = record.value();
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1).replace("\\\"", "\"");
            }
            TransactionEvent transactionEvent = gson.fromJson(json, TransactionEvent.class);
            log.info("Mensaje deserializado: {}", transactionEvent);

            transactionEventHandler.handleTransactionEvent(transactionEvent);

        } catch (JsonSyntaxException e) {
            log.error("Error de sintaxis en JSON durante la deserialización: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado durante la deserialización: ", e);
        }
    }
}