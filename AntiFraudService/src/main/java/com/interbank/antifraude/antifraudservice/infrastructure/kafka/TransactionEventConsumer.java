package com.interbank.antifraude.antifraudservice.infrastructure.kafka;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.interbank.antifraude.antifraudservice.application.services.impl.AntiFraudService;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventConsumer.class);
    private final Gson gson;
    private final AntiFraudService antiFraudService; 

    public TransactionEventConsumer(AntiFraudService antiFraudService,
                                    @Value("${spring.kafka.topic.transaction}") String transactionTopic) {
        this.gson = new Gson();
        this.antiFraudService = antiFraudService; 
    }

    @KafkaListener(topics = "${spring.kafka.topic.transaction}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Mensaje recibido crudo desde Kafka: {}", record.value());
        try {
            String json = record.value();
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1).replace("\\\"", "\"");
            }
            EventTrxDomain event = gson.fromJson(json, EventTrxDomain.class);
            log.info("Mensaje deserializado: {}", event);
 
            antiFraudService.consumeEvent(event);

        } catch (JsonSyntaxException e) {
            log.error("Error de sintaxis en JSON durante la deserialización: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado durante la deserialización: ", e);
        }
    }
}
