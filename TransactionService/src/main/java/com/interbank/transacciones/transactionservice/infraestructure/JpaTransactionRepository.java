package com.interbank.transacciones.transactionservice.infraestructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}