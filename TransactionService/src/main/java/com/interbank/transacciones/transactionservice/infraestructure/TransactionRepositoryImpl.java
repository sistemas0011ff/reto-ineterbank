package com.interbank.transacciones.transactionservice.infraestructure;

import com.interbank.transacciones.transactionservice.domain.DomainTransaction;
import com.interbank.transacciones.transactionservice.domain.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JpaTransactionRepository jpaTransactionRepository;

    @Override
    public DomainTransaction save(DomainTransaction transaction) {
        TransactionEntity entity = toEntity(transaction);
        TransactionEntity savedEntity = jpaTransactionRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public List<DomainTransaction> findAll() {
        return jpaTransactionRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public DomainTransaction findById(UUID id) {
        return jpaTransactionRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    private TransactionEntity toEntity(DomainTransaction transaction) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionExternalId(transaction.getTransactionExternalId());
        entity.setAccountExternalIdDebit(transaction.getAccountExternalIdDebit());
        entity.setAccountExternalIdCredit(transaction.getAccountExternalIdCredit());
        entity.setTransferTypeId(transaction.getTransferTypeId());
        entity.setValue(transaction.getValue());
        entity.setTransactionStatus(transaction.getTransactionStatus());
        entity.setTransactionType(transaction.getTransactionType());
        entity.setCreatedAt(transaction.getCreatedAt());
        return entity;
    }

    private DomainTransaction toDomain(TransactionEntity entity) {
        DomainTransaction transaction = new DomainTransaction();
        transaction.setTransactionExternalId(entity.getTransactionExternalId());
        transaction.setAccountExternalIdDebit(entity.getAccountExternalIdDebit());
        transaction.setAccountExternalIdCredit(entity.getAccountExternalIdCredit());
        transaction.setTransferTypeId(entity.getTransferTypeId());
        transaction.setValue(entity.getValue());
        transaction.setTransactionStatus(entity.getTransactionStatus());
        transaction.setTransactionType(entity.getTransactionType());
        transaction.setCreatedAt(entity.getCreatedAt());
        return transaction;
    }
}