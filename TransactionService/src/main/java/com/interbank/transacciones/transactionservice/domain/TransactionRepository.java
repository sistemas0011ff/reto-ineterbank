package com.interbank.transacciones.transactionservice.domain;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    DomainTransaction save(DomainTransaction transaction);
    List<DomainTransaction> findAll();
    DomainTransaction findById(UUID id);
}