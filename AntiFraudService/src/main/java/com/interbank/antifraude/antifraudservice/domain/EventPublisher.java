package com.interbank.antifraude.antifraudservice.domain;

public interface EventPublisher {
    void publishEvent(EventTrxDomain event);
}
