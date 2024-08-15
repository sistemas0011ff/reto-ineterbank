package com.interbank.transacciones.transactionservice.util;

public class TransactionServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public TransactionServiceException(String message) {
        super(message);
    }

    public TransactionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}