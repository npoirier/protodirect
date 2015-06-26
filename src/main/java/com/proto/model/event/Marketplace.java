package com.proto.model.event;

/**
 * <marketplace>
 * <baseUrl>https://acme.appdirect.com</baseUrl>
 * <partner>ACME</partner>
 * </marketplace>
 */
public class Marketplace {
    private String baseUrl;
    private String partner;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
