package com.proto.model.event;

/**
 *
 */
public enum NoticeType {
    /**
     * A DEACTIVATED notice means the account is deactivated. It is recommended that all access to the ISV's application
     * be suspended (but not deleted). Account deactivation may occur if, for example, the account holder is overdue in
     * making a payment or if abuse is detected.
     */
    DEACTIVATED,
    /**
     * A REACTIVATED notice means an account should be considered active and receive its typical access. This status will
     * usually indicate that the account holder has paid an overdue invoice.
     */
    REACTIVATED,
    /**
     * A CLOSED notice means that the account has been in a SUSPENDED or FREE_TRIAL_EXPIRED state for a period of time
     * exceeding the grace period (typically 1 or 2 months but it may vary by marketplace), and that it should be deleted
     * by the ISV. In most cases, this event should trigger the same code on the ISV as the SUBSCRIPTION_CANCELLED event.
     */
    CLOSED,
    /**
     * An UPCOMING_INVOICE notice informs a vendor that there is an upcoming invoice that will be computed for this account.
     * This will be issued 24 hours prior to the calculation of the account's outstanding bill. The intention of this notice
     * is to give a vendor the opportunity to update the AppDirect-powered marketplace with any usage information via the
     * Billing Usage API, which will be included on the upcoming invoice.
     */
    UPCOMING_INVOICE

}
