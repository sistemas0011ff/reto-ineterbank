package com.interbank.transacciones.transactionservice.domain;

public interface TransactionEventHandler {
    void handleTransactionEvent(TransactionEvent event);
}