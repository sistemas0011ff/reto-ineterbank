package com.interbank.transacciones.transactionservice.domain;

public class TransactionEvent {
    private String transactionExternalId;
    private String accountExternalIdDebit;
    private String accountExternalIdCredit;
    private int transferTypeId;
    private double value;
    private String transactionStatus;
    private String transactionType; 
	
    public TransactionEvent() {
    }

    public TransactionEvent(String transactionExternalId , String accountExternalIdDebit, String accountExternalIdCredit, int transferTypeId, double value, String transactionStatus, String transactionType) {
        this.transactionExternalId = transactionExternalId;
        this.accountExternalIdDebit = accountExternalIdDebit;
        this.accountExternalIdCredit = accountExternalIdCredit;
        this.transferTypeId = transferTypeId;
        this.value = value;
        this.transactionStatus = transactionStatus;
        this.transactionType = transactionType;     
    }

    public String getTransactionExternalId() {
        return transactionExternalId;
    }

    public void setTransactionExternalId(String transactionExternalId) {
        this.transactionExternalId = transactionExternalId;
    }

    public String getAccountExternalIdDebit() {
        return accountExternalIdDebit;
    }

    public void setAccountExternalIdDebit(String accountExternalIdDebit) {
        this.accountExternalIdDebit = accountExternalIdDebit;
    }

    public String getAccountExternalIdCredit() {
        return accountExternalIdCredit;
    }

    public void setAccountExternalIdCredit(String accountExternalIdCredit) {
        this.accountExternalIdCredit = accountExternalIdCredit;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

}