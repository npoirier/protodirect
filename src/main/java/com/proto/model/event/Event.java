package com.proto.model.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Event {
    EventType type;
    EventFlag flag;
    private Marketplace marketplace;
    private User creator;
    private Payload payload;
    private String returnUrl;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Marketplace getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventFlag getFlag() {
        return flag;
    }

    public void setFlag(EventFlag flag) {
        this.flag = flag;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public enum EventType {
        /**
         * fired by AppDirect when a user buys an app from AppDirect
         */
        SUBSCRIPTION_ORDER,
        /**
         * fired by AppDirect when a user upgrades/downgrades/modifies an existing subscription
         */
        SUBSCRIPTION_CHANGE,
        /**
         * fired by AppDirect when a user cancels a subscription
         */
        SUBSCRIPTION_CANCEL,
        /**
         * fired by AppDirect when a subscription goes overdue or delinquent
         */
        SUBSCRIPTION_NOTICE,
        /**
         * fired by AppDirect when a user assigns a user to an app
         */
        USER_ASSIGNMENT,
        /**
         * fired by AppDirect when a user unassigns a user from an app
         */
        USER_UNASSIGNMENT
    }

    public enum EventFlag {
        STATELESS, DEVELOPMENT
    }
}
