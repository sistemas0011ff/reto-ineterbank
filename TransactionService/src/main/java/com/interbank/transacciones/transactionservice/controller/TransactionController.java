package com.interbank.transacciones.transactionservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.interbank.transacciones.transactionservice.application.dto.TransactionRequestDTO;
import com.interbank.transacciones.transactionservice.application.dto.TransactionResponseDTO;
import com.interbank.transacciones.transactionservice.application.services.TransactionService;
import com.interbank.transacciones.transactionservice.util.ApiResponse;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> createTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO = transactionService.createTransaction(transactionRequestDTO);
        
        ApiResponse<TransactionResponseDTO> response = new ApiResponse<>(
                true,
                "Transaction created successfully",
                transactionResponseDTO,
                HttpStatus.CREATED.value()
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> getTransactionById(@PathVariable UUID id) {
        TransactionResponseDTO transactionResponseDTO = transactionService.findTransactionStatusById(id);
        
        if (transactionResponseDTO == null) {
            ApiResponse<TransactionResponseDTO> response = new ApiResponse<>(
                    false,
                    "Transaction not found",
                    null,
                    HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
        ApiResponse<TransactionResponseDTO> response = new ApiResponse<>(
                true,
                "Transaction retrieved successfully",
                transactionResponseDTO,
                HttpStatus.OK.value()
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}