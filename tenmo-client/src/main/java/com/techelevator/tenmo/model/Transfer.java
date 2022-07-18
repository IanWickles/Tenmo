package com.techelevator.tenmo.model;

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

    public Transfer(long receiverId, BigDecimal transferAmount) {
        this.receiverAccountId = receiverId;
        this.transferAmount = transferAmount;
    }

    public Transfer() {};

    public long getReceiverAccountId() {
        return receiverAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public long getTransferId() {
        return transferId;
    }

    public long getTransferTypeId() {
        return transferTypeId;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public long getSenderAccountId() {
        return senderAccountId;
    }

    public long getSenderUserId() {
        return senderUserId;
    }

    public long getReceiverUserId() {
        return receiverUserId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }


}
