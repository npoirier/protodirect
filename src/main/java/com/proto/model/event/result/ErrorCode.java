package com.proto.model.event.result;

/**
 * When the Application Vendor communicates a failure while processing information from AppDirect,
 * one of the following error codes must be returned
 */
public enum ErrorCode {
    /**
     * This error code is typically used when AppDirect admins try to buy subscriptions for apps they have already
     * purchased directly from the Application Vendor. In this scenario, we'll show users an error message and prompt
     * them to link their accounts.
     */
    USER_ALREADY_EXISTS,
    /**
     * This error code is typically used when AppDirect admins try to unassign users not found in the Application Vendor's account.
     */
    USER_NOT_FOUND,
    /**
     * This error code is typically used when AppDirect admins try to add or remove users from an account not found in
     * the Application Vendor's records.
     */
    ACCOUNT_NOT_FOUND,
    /**
     * This error code is typically used when AppDirect admins try to assign users beyond the limit of the number of seats
     * available. AppDirect will typically prevent that from happening by monitoring app usage.
     */
    MAX_USERS_REACHED,
    /**
     * This error code is returned when users try any action that is not authorized for that particular application.
     * For example, if an application does not allow the original creator to be unassigned.
     */
    UNAUTHORIZED,
    /**
     * This error code is returned when a user manually interrupts the operation (clicking cancel on the account creation page, etc.).
     */
    OPERATION_CANCELED,
    /**
     * This error code is returned when the vendor endpoint is not currently configured.
     */
    CONFIGURATION_ERROR,
    /**
     * This error code is returned when the vendor was unable to process the event fetched from AppDirect.
     */
    INVALID_RESPONSE,
    /**
     * This error code may be used when none of the other error codes apply.
     */
    UNKNOWN_ERROR,
    /**
     * This error code is returned when the vendor was unable to process the event because the service is under provisioning.
     */
    PENDING;
}
