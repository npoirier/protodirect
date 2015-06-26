package com.proto.model.event;

/**
 * <accountIdentifier>dummy-account</accountIdentifier>
 * <status>ACTIVE</status>
 */
public class Account {
    private String accountIdentifier;
    private AccountStatus status;

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
