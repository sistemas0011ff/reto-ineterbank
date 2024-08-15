package com.interbank.transacciones.transactionservice;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.interbank.transacciones.transactionservice.application.dto.TransactionRequestDTO;
import com.interbank.transacciones.transactionservice.application.dto.TransactionResponseDTO;
import com.interbank.transacciones.transactionservice.application.services.impl.TransactionServiceImpl;
import com.interbank.transacciones.transactionservice.domain.DomainTransaction;
import com.interbank.transacciones.transactionservice.domain.MessageProducer;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;
import com.interbank.transacciones.transactionservice.domain.TransactionRepository;
import com.interbank.transacciones.transactionservice.util.TransactionServiceException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountExternalIdDebit(UUID.randomUUID());
        requestDTO.setAccountExternalIdCredit(UUID.randomUUID());
        requestDTO.setTransferTypeId(1);
        requestDTO.setValue(100.0);

        DomainTransaction savedTransaction = new DomainTransaction();
        savedTransaction.setTransactionExternalId(UUID.randomUUID());
        savedTransaction.setAccountExternalIdDebit(requestDTO.getAccountExternalIdDebit());
        savedTransaction.setAccountExternalIdCredit(requestDTO.getAccountExternalIdCredit());
        savedTransaction.setTransferTypeId(requestDTO.getTransferTypeId());
        savedTransaction.setValue(requestDTO.getValue());
        savedTransaction.setTransactionStatus("pendiente");
        savedTransaction.setTransactionType("transferencia");

        when(transactionRepository.save(ArgumentMatchers.any(DomainTransaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionResponseDTO responseDTO = transactionService.createTransaction(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(savedTransaction.getTransactionExternalId(), responseDTO.getTransactionExternalId());
        verify(transactionRepository, times(1)).save(any(DomainTransaction.class));
        verify(messageProducer, times(1)).sendMessage(any(TransactionEvent.class));
    }

    @Test
    void testCreateTransaction_ThrowsExceptionWhenAccountExternalIdIsNull() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountExternalIdDebit(null);
        requestDTO.setAccountExternalIdCredit(UUID.randomUUID());
        requestDTO.setValue(100.0);

        // Act & Assert
        assertThrows(TransactionServiceException.class, () -> transactionService.createTransaction(requestDTO));
        verify(transactionRepository, never()).save(any(DomainTransaction.class));
        verify(messageProducer, never()).sendMessage(any(TransactionEvent.class));
    }

    @Test
    void testCreateTransaction_ThrowsExceptionWhenValueIsInvalid() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountExternalIdDebit(UUID.randomUUID());
        requestDTO.setAccountExternalIdCredit(UUID.randomUUID());
        requestDTO.setValue(0.0);

        // Act & Assert
        assertThrows(TransactionServiceException.class, () -> transactionService.createTransaction(requestDTO));
        verify(transactionRepository, never()).save(any(DomainTransaction.class));
        verify(messageProducer, never()).sendMessage(any(TransactionEvent.class));
    }

    @Test
    void testFindTransactionById() {
        // Arrange
        UUID id = UUID.randomUUID();
        DomainTransaction transaction = new DomainTransaction();
        transaction.setTransactionExternalId(id);
        when(transactionRepository.findById(id)).thenReturn(transaction);

        // Act
        DomainTransaction result = transactionService.findTransactionById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getTransactionExternalId());
        verify(transactionRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateTransactionStatus() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        TransactionEvent event = new TransactionEvent();
        event.setTransactionExternalId(transactionId.toString());
        event.setTransactionStatus("completado");

        DomainTransaction transaction = new DomainTransaction();
        transaction.setTransactionExternalId(transactionId);
        transaction.setTransactionStatus("pendiente");

        when(transactionRepository.findById(transactionId)).thenReturn(transaction);

        // Act
        transactionService.updateTransactionStatus(event);

        // Assert
        assertEquals("completado", transaction.getTransactionStatus());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testHandleTransactionEvent() {
        // Arrange
        TransactionEvent event = new TransactionEvent();
        event.setTransactionExternalId(UUID.randomUUID().toString());
        event.setTransactionStatus("completado");

        // Act
        transactionService.handleTransactionEvent(event);

        // Assert
        verify(transactionRepository, times(1)).findById(UUID.fromString(event.getTransactionExternalId()));
    }
}
