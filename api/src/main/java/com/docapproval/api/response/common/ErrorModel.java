package com.docapproval.api.response.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class ErrorModel {

    private final String error;

    public ErrorModel(HttpStatus returnStatusCode) {
        error = returnStatusCode.toString();
    }

    public ErrorModel(HttpStatus returnStatusCode, String errMsg) {
        StringBuilder jsonStrBuilder = new StringBuilder();
        jsonStrBuilder.append(returnStatusCode.toString()).append(" (").append(errMsg).append(")");
        error = jsonStrBuilder.toString();
    }

    public ErrorModel(HttpStatus returnStatusCode, BindingResult bindingResult) {
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder jsonStrBuilder = new StringBuilder();
        jsonStrBuilder.append(returnStatusCode.toString()).append(" (");
        fieldErrors.forEach(fieldError -> jsonStrBuilder.append(fieldError.getDefaultMessage()));
        jsonStrBuilder.append(")");
        error = jsonStrBuilder.toString();
    }
}
