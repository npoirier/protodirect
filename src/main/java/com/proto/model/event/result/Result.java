package com.proto.model.event.result;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Result {

    private boolean success;

    /**
     * An optional message containing information about the result of the operation.
     */
    private String message;

    /**
     * If "success" is false, this should contain one of the supported error codes
     */
    private ErrorCode errorCode;

    /**
     * A sequence of characters that will be use to identify the account.
     */
    private String accountIdentifier;

    public Result() {

    }

    Result(boolean success, String message, ErrorCode errorCode, String accountIdentifier) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.accountIdentifier = accountIdentifier;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (success != result.success) return false;
        if (message != null ? !message.equals(result.message) : result.message != null) return false;
        if (errorCode != result.errorCode) return false;
        return !(accountIdentifier != null ? !accountIdentifier.equals(result.accountIdentifier) : result.accountIdentifier != null);

    }

    @Override
    public int hashCode() {
        int result = (success ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
        result = 31 * result + (accountIdentifier != null ? accountIdentifier.hashCode() : 0);
        return result;
    }
}
