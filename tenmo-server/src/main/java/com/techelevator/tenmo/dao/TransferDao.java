package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    boolean transferMoney(long senderId, Transfer transfer);

    public List<Transfer> viewTransfers(long userId);

   public Transfer transferDetails(long transactionId);
}
