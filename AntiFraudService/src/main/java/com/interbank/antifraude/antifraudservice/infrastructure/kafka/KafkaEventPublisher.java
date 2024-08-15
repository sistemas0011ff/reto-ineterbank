package com.interbank.antifraude.antifraudservice.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.interbank.antifraude.antifraudservice.domain.EventPublisher;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain;

@Component
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;
    private final String resultTopic;

    @Autowired
    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, 
                               @Value("${spring.kafka.topic.transaction-result}") String resultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.gson = new Gson();
        this.resultTopic = resultTopic;
    }

    @Override
    public void publishEvent(EventTrxDomain event) {
        // Convertir el objeto a JSON
        String jsonEvent = gson.toJson(event);
        // Enviar el JSON como texto a Kafka
        kafkaTemplate.send(resultTopic, jsonEvent);
    }
}