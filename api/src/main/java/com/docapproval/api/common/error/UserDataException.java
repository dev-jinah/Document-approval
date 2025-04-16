package com.docapproval.api.common.error;

import org.springframework.http.HttpStatus;

public class UserDataException extends RuntimeException {

    private final HttpStatus returnStatusCode;

    public UserDataException(String message) {
        super(message);
        this.returnStatusCode = HttpStatus.NOT_FOUND;
    }

    public UserDataException(String message, HttpStatus returnStatusCode) {
        super(message);
        this.returnStatusCode = returnStatusCode;
    }

    public HttpStatus getStatusCode() {
        return returnStatusCode;
    }
}
