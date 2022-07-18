package com.techelevator.tenmo.model;

public class Account {

    private long accountId;
    private long userId;
    private User user;

    public Account() { }

    public Account(long accountId, long userId) {
        this.accountId = accountId;
        this.userId = userId;
    }

    public Account(long accountId, long userId, User user) {
        this.accountId = accountId;
        this.userId = userId;
        this.user = user;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }
}
