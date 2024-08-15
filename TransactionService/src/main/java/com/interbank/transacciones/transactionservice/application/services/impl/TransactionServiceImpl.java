package com.interbank.transacciones.transactionservice.application.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interbank.transacciones.transactionservice.application.dto.TransactionRequestDTO;
import com.interbank.transacciones.transactionservice.application.dto.TransactionResponseDTO;
import com.interbank.transacciones.transactionservice.application.services.TransactionService;
import com.interbank.transacciones.transactionservice.domain.DomainTransaction;
import com.interbank.transacciones.transactionservice.domain.MessageProducer;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;
import com.interbank.transacciones.transactionservice.domain.TransactionRepository;
import com.interbank.transacciones.transactionservice.util.TransactionServiceException;

import jakarta.transaction.InvalidTransactionException;

import com.interbank.transacciones.transactionservice.domain.TransactionEventHandler;

@Service
public class TransactionServiceImpl implements TransactionService, TransactionEventHandler {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private MessageProducer messageProducer;

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO) {
    	
    	if (requestDTO.getAccountExternalIdDebit() == null || requestDTO.getAccountExternalIdCredit() == null) {
            throw new TransactionServiceException("Las cuentas no pueden ser nulas");
        }
        if (requestDTO.getValue() <= 0) {
            throw new TransactionServiceException("El monto debe ser mayor a 0");
        }
    	
        DomainTransaction transaction = new DomainTransaction();
        transaction.setAccountExternalIdDebit(requestDTO.getAccountExternalIdDebit());
        transaction.setAccountExternalIdCredit(requestDTO.getAccountExternalIdCredit());
        transaction.setTransferTypeId(requestDTO.getTransferTypeId());
        transaction.setValue(requestDTO.getValue());
        transaction.setTransactionStatus("pendiente");
        transaction.setTransactionType("transferencia");

        DomainTransaction savedTransaction = transactionRepository.save(transaction);
        
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setTransactionExternalId(savedTransaction.getTransactionExternalId().toString());
        transactionEvent.setAccountExternalIdDebit(savedTransaction.getAccountExternalIdDebit().toString());
        transactionEvent.setAccountExternalIdCredit(savedTransaction.getAccountExternalIdCredit().toString());
        transactionEvent.setTransferTypeId(savedTransaction.getTransferTypeId());
        transactionEvent.setValue(savedTransaction.getValue());
        transactionEvent.setTransactionStatus(savedTransaction.getTransactionStatus());
        transactionEvent.setTransactionType(savedTransaction.getTransactionType());
        

        messageProducer.sendMessage(transactionEvent);

        return toResponseDTO(savedTransaction);
    }

    @Override
    public DomainTransaction findTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }
    
    private TransactionResponseDTO toResponseDTO(DomainTransaction transaction) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setTransactionExternalId(transaction.getTransactionExternalId());
        responseDTO.setAccountExternalIdDebit(transaction.getAccountExternalIdDebit());
        responseDTO.setAccountExternalIdCredit(transaction.getAccountExternalIdCredit());
        responseDTO.setTransferTypeId(transaction.getTransferTypeId());
        responseDTO.setValue(transaction.getValue());
        responseDTO.setTransactionStatus(transaction.getTransactionStatus());
        responseDTO.setTransactionType(transaction.getTransactionType());
        responseDTO.setCreatedAt(transaction.getCreatedAt());
        return responseDTO;
    }
    
    @Override
    public void updateTransactionStatus(TransactionEvent event) {
        DomainTransaction transaction = transactionRepository.findById(UUID.fromString(event.getTransactionExternalId()));
        if (transaction != null) {
            transaction.setTransactionStatus(event.getTransactionStatus());
            transactionRepository.save(transaction);
        }
    }

    @Override
    public void handleTransactionEvent(TransactionEvent event) {
        updateTransactionStatus(event);
    }

    @Override
    public TransactionResponseDTO findTransactionStatusById(UUID id) {
        DomainTransaction transaction = transactionRepository.findById(id);
        if (transaction != null) {
            return toResponseDTO(transaction);
        } else {
            return null;   
        }
    }
}