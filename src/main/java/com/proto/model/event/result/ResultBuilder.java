package com.proto.model.event.result;

public class ResultBuilder {
    private boolean success;
    private String message;
    private ErrorCode errorCode;
    private String accountIdentifier;

    public ResultBuilder setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ResultBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResultBuilder setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ResultBuilder setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
        return this;
    }

    public Result createResult() {
        return new Result(success, message, errorCode, accountIdentifier);
    }
}
