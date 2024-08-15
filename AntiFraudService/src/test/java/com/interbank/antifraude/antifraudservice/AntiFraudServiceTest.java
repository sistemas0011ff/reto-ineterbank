package com.interbank.antifraude.antifraudservice;

import com.interbank.antifraude.antifraudservice.application.services.impl.AntiFraudService;
import com.interbank.antifraude.antifraudservice.domain.EventPublisher;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class AntiFraudServiceTest {

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private AntiFraudService antiFraudService;

    private EventTrxDomain eventTrxDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventTrxDomain = new EventTrxDomain();
        eventTrxDomain.setTransactionExternalId("1234");
        eventTrxDomain.setValue(1500);
    }

    @Test
    void testConsumeEvent_RejectedTransaction() {
        eventTrxDomain.setValue(1500);

        antiFraudService.consumeEvent(eventTrxDomain);

        ArgumentCaptor<EventTrxDomain> eventCaptor = ArgumentCaptor.forClass(EventTrxDomain.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        EventTrxDomain capturedEvent = eventCaptor.getValue();
        assertEquals("rechazado", capturedEvent.getTransactionStatus());
    }

    @Test
    void testConsumeEvent_ApprovedTransaction() {
        eventTrxDomain.setValue(500);

        antiFraudService.consumeEvent(eventTrxDomain);

        ArgumentCaptor<EventTrxDomain> eventCaptor = ArgumentCaptor.forClass(EventTrxDomain.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        EventTrxDomain capturedEvent = eventCaptor.getValue();
        assertEquals("aprobado", capturedEvent.getTransactionStatus());
    }
}