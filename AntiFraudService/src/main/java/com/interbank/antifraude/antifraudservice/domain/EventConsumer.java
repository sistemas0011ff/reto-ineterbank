package com.interbank.antifraude.antifraudservice.domain;

public interface EventConsumer {
    void consumeEvent(EventTrxDomain event);
}