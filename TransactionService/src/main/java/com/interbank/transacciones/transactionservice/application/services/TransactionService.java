package com.interbank.transacciones.transactionservice.application.services;

import java.util.UUID;

import com.interbank.transacciones.transactionservice.application.dto.TransactionRequestDTO;
import com.interbank.transacciones.transactionservice.application.dto.TransactionResponseDTO;
import com.interbank.transacciones.transactionservice.domain.DomainTransaction;
import com.interbank.transacciones.transactionservice.domain.TransactionEvent;

public interface TransactionService {

    /**
     * Crea una nueva transacción basándose en los datos de la solicitud y la persiste en la base de datos.
     *
     * @param requestDTO Los datos de la solicitud para crear una transacción.
     * @return La transacción creada.
     */
    TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO);

    /**
     * Encuentra una transacción por su ID.
     *
     * @param id El ID de la transacción.
     * @return La transacción correspondiente al ID, o null si no se encuentra.
     */
    DomainTransaction findTransactionById(UUID id);
    
    /**
     * Encuentra una transacción por su ID.
     *
     * @param id El ID de la transacción.
     * @return La transacción correspondiente al ID, o null si no se encuentra.
     */
    TransactionResponseDTO findTransactionStatusById(UUID id);

    /**
     * Actualiza el estado de una transacción basándose en un evento recibido.
     *
     * @param event El evento de transacción que contiene los nuevos datos.
     */
    void updateTransactionStatus(TransactionEvent event);
    
    
}
