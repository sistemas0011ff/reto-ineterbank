package com.interbank.antifraude.antifraudservice.application.services.impl;

import com.interbank.antifraude.antifraudservice.domain.EventConsumer;
import com.interbank.antifraude.antifraudservice.domain.EventPublisher;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudService implements EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AntiFraudService.class);
    private final EventPublisher eventPublisher;

    @Autowired
    public AntiFraudService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void consumeEvent(EventTrxDomain event) { 
        if (event.getValue() > 1000) {
            event.setTransactionStatus("rechazado");
        } else {
            event.setTransactionStatus("aprobado");
        }
 
        eventPublisher.publishEvent(event);
        log.info("Resultado de la validación enviado a Kafka: {}", event);
    }
}