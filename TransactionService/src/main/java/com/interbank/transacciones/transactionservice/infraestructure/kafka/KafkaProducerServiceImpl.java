package com.interbank.transacciones.transactionservice.infraestructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interbank.transacciones.transactionservice.domain.MessageProducer;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements MessageProducer {
   
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);
    
    @Value("${kafka.topic.transaction:trx-topic}")
    private String topic;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(TransactionEvent event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(event);
            logger.info("Intentando enviar evento de transacción a Kafka: {}", message);
            kafkaTemplate.send(topic, message);
            logger.info("Evento de transacción enviado a Kafka con éxito");
        } catch (Exception e) {
            logger.error("Error al serializar el evento: {}", e.getMessage(), e);
        }
    }
}    