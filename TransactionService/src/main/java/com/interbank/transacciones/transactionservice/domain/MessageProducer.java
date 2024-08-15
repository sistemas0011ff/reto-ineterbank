package com.interbank.transacciones.transactionservice.domain;


public interface MessageProducer {
    void sendMessage(TransactionEvent event);
}