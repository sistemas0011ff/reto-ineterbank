package com.interbank.transacciones.transactionservice;

import com.interbank.transacciones.transactionservice.application.dto.TransactionRequestDTO;
import com.interbank.transacciones.transactionservice.application.dto.TransactionResponseDTO;
import com.interbank.transacciones.transactionservice.application.services.TransactionService;
import com.interbank.transacciones.transactionservice.controller.TransactionController;
import com.interbank.transacciones.transactionservice.util.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 

import java.util.UUID;
 

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountExternalIdDebit(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        requestDTO.setAccountExternalIdCredit(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        requestDTO.setTransferTypeId(1);
        requestDTO.setValue(100.0);

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setTransactionExternalId(UUID.randomUUID());
        responseDTO.setTransactionStatus("SUCCESS");

        when(transactionService.createTransaction(any(TransactionRequestDTO.class))).thenReturn(responseDTO);

        // Act
        ResponseEntity<ApiResponse<TransactionResponseDTO>> response = transactionController.createTransaction(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Transaction created successfully", response.getBody().getMessage());
        assertEquals(responseDTO, response.getBody().getData());
        verify(transactionService, times(1)).createTransaction(any(TransactionRequestDTO.class));
    }

    @Test
    void testCreateTransaction_Failure() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountExternalIdDebit(UUID.randomUUID()); // Se utiliza un UUID inválido simulando el error
        requestDTO.setAccountExternalIdCredit(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        requestDTO.setTransferTypeId(1);
        requestDTO.setValue(100.0);

        when(transactionService.createTransaction(any(TransactionRequestDTO.class))).thenThrow(new RuntimeException("Transaction failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> transactionController.createTransaction(requestDTO));
        verify(transactionService, times(1)).createTransaction(any(TransactionRequestDTO.class));
    }
}