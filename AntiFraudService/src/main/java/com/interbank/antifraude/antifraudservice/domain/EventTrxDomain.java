package com.interbank.antifraude.antifraudservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventTrxDomain {
    
    private String transactionExternalId;
    private String accountExternalIdDebit;
    private String accountExternalIdCredit;
    private int transferTypeId;
    private double value;
    private String transactionStatus;
    private String transactionType; 

    // Constructor vacío
    public EventTrxDomain() {}

    // Constructor con todos los campos
    @JsonCreator
    public EventTrxDomain(
            @JsonProperty("transactionExternalId") String transactionExternalId,
            @JsonProperty("accountExternalIdDebit") String accountExternalIdDebit,
            @JsonProperty("accountExternalIdCredit") String accountExternalIdCredit,
            @JsonProperty("transferTypeId") int transferTypeId,
            @JsonProperty("value") double value,
            @JsonProperty("transactionStatus") String transactionStatus,
            @JsonProperty("transactionType") String transactionType) {
        this.transactionExternalId = transactionExternalId;
        this.accountExternalIdDebit = accountExternalIdDebit;
        this.accountExternalIdCredit = accountExternalIdCredit;
        this.transferTypeId = transferTypeId;
        this.value = value;
        this.transactionStatus = transactionStatus;
        this.transactionType = transactionType;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        return "EventTrxDomain{" +
                "transactionExternalId='" + transactionExternalId + '\'' +
                ", accountExternalIdDebit='" + accountExternalIdDebit + '\'' +
                ", accountExternalIdCredit='" + accountExternalIdCredit + '\'' +
                ", transferTypeId=" + transferTypeId +
                ", value=" + value +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
  
    
    

}
