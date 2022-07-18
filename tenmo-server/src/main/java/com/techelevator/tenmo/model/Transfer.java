package com.techelevator.tenmo.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class Transfer {

    private long transferId;
    private long transferTypeId;
    private long transferStatusId;
    private long senderAccountId;
    private long receiverAccountId;
    private BigDecimal transferAmount;
    private long senderUserId;
    private long receiverUserId;
    private String senderUsername;
    private String receiverUsername;

    public Transfer() { }

    public Transfer(long senderAccountId, long receiverAccountId, BigDecimal transferAmount) {
        this.receiverAccountId = receiverAccountId;
        this.transferAmount = transferAmount;
    }

    public Transfer(long transferId, long transferTypeId, long transferStatusId, long senderAccountId, long receiverAccountId, BigDecimal transferAmount, long senderUserId, long receiverUserId, String senderUsername, String receiverUsername) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.transferAmount = transferAmount;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    public long getReceiverId() {
        return receiverAccountId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverAccountId = receiverId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(long senderId) {
        this.senderAccountId = senderAccountId;
    }

    public long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }
}
