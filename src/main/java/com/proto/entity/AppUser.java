package com.proto.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class AppUser {
    private String email;
    private String firstName;
    private String language;
    private String lastName;
    private String openId;
    private String uuid;
    private String accountIdentifier;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    @Id
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", language='" + language + '\'' +
                ", lastName='" + lastName + '\'' +
                ", openId='" + openId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", accountIdentifier='" + accountIdentifier + '\'' +
                '}';
    }
}
