package com.proto.model.event;

/**
 * <email>test-email+creator@appdirect.com</email>
 * <firstName>DummyCreatorFirst</firstName>
 * <language>fr</language>
 * <lastName>DummyCreatorLast</lastName>
 * <openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId>
 * <uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid>
 */
public class User {
    private String email;
    private String firstName;
    private String language;
    private String lastName;
    private String openId;
    private String uuid;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
