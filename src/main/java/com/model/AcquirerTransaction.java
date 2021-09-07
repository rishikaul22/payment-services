package com.model;

import java.io.Serializable;

public class AcquirerTransaction implements Serializable {
    private String id;
    private String customerName, customerCardNumber;
    private String merchantName, merchantAccountId;
    private long amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerCardNumber='" + customerCardNumber + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantAccountId='" + merchantAccountId + '\'' +
                ", amount=" + amount +
                '}';
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCardNumber() {
        return customerCardNumber;
    }

    public void setCustomerCardNumber(String customerCardNumber) {
        this.customerCardNumber = customerCardNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public void setMerchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
