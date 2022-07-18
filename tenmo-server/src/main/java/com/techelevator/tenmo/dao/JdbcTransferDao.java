package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component

public class JdbcTransferDao implements TransferDao {

    private final String ALL_COLUMNS = " transfer_id, transfer_type_id, transfer_status_id, " +
            "account_from as sender_account_id, account_to as receiver_account_id, amount, " +
            "t_from.user_id as sender_user_id, t_from.username as sender_username, t_to.user_id as receiver_user_id, " +
            "t_to.username as receiver_username\n";

    private final String JOIN_FROM_AND_TO = "JOIN account as afrom on afrom.account_id = transfer.account_from " +
            "JOIN tenmo_user as t_from on afrom.user_id = t_from.user_id " +
            "JOIN account as ato on ato.account_id = transfer.account_to " +
            "JOIN tenmo_user as t_to on ato.user_id = t_to.user_id ";

    private JdbcTemplate jdbcTemplate;
    private User user;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean transferMoney(long senderId, Transfer transfer) {
        String sql = "BEGIN TRANSACTION; \n" +
                //takes money from one account and adds to another
                "UPDATE account SET balance = balance - ? WHERE user_id = ?; \n" +
                "UPDATE account SET balance = balance + ? WHERE user_id = ?; \n" +
                //log transaction details into the transfer table
                "INSERT INTO transfer" +
                "(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(default,2,2,(SELECT account_id FROM account WHERE user_id = ?)," +
                "(SELECT account_id FROM account WHERE user_id =?),?); " +
                // transaction/commit means all actions or nothing
                "COMMIT;";
        try {
            jdbcTemplate.update(sql, transfer.getTransferAmount(), senderId, transfer.getTransferAmount(),
                    transfer.getReceiverAccountId(), senderId, transfer.getReceiverAccountId(), transfer.getTransferAmount());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Transfer> viewTransfers(long userID) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT " +
                ALL_COLUMNS +
                "FROM transfer\n" +
                JOIN_FROM_AND_TO +
                "WHERE account_to = (SELECT account_id FROM account WHERE user_id = ?)\n" +
                "UNION\n" +
                "SELECT " +
                ALL_COLUMNS +
                "FROM transfer\n" +
                JOIN_FROM_AND_TO +
                "WHERE account_from = (SELECT account_id FROM account WHERE user_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID, userID);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer transferDetails(long transferId) { //user chooses the transaction id
        Transfer transfer = new Transfer();
        String sql = "SELECT" +
                ALL_COLUMNS +
                "FROM transfer " +
                JOIN_FROM_AND_TO +
                "WHERE transfer_id =  ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            while (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getLong("transfer_id"));
        transfer.setTransferTypeId(rowSet.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getLong("transfer_status_id"));
        transfer.setSenderAccountId(rowSet.getLong("sender_account_id"));
        transfer.setReceiverAccountId(rowSet.getLong("receiver_account_id"));
        transfer.setTransferAmount(rowSet.getBigDecimal("amount"));
        transfer.setSenderUserId(rowSet.getLong("sender_user_id"));
        transfer.setSenderUsername(rowSet.getString("sender_username"));
        transfer.setReceiverUserId(rowSet.getLong("receiver_user_id"));
        transfer.setReceiverUsername(rowSet.getString("receiver_username"));
        return transfer;
    }
}
