package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.rowset.JdbcRowSet;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User user;

    @Override
    //retrieves balance of current user
    public double getBalance(String username) {
        String sql = "SELECT balance FROM account " +
                "JOIN tenmo_user on account.user_id = tenmo_user.user_id " +
                "WHERE username = ?;";
        Double currentBalance = jdbcTemplate.queryForObject(sql, Double.class, username);
        if(username != null) {
            return currentBalance;
        } else {
            return 0.00;
        }
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getLong("account_id"));
        account.setUserId(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getDouble("balance"));
        return account;
    }
}
