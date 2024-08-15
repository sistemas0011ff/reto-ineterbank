package com.interbank.transacciones.transactionservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;
import com.interbank.transacciones.transactionservice.infraestructure.kafka.KafkaProducerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class KafkaProducerServiceImplTest {

   
	@Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducerServiceImpl kafkaProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(kafkaProducerService, "topic", "trx-topic");
    }

    @Test
    void testSendMessage_Success() throws JsonProcessingException {
    
        TransactionEvent event = new TransactionEvent();
        event.setTransactionExternalId("1234");
        event.setTransactionStatus("pendiente");

        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(event);

       
        kafkaProducerService.sendMessage(event);

      
        verify(kafkaTemplate, times(1)).send("trx-topic", message);
    }

}
