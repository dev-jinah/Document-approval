package com.docapproval.api.service;

import com.docapproval.api.response.common.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    private HttpStatus returnStatusCode;

    private String errorMessage;

    protected BaseService() {
        returnStatusCode = HttpStatus.OK;
        errorMessage = "";
    }

    protected void setErrorResponse(HttpStatus status, String errMsg) {
        returnStatusCode = status;
        errorMessage = errMsg;
    }

    public HttpStatus getStatusCode() {return returnStatusCode;}
    public String getErrorMessage() {return errorMessage;}
    public ResponseEntity<Object> getErrorResponse() {
        return new ResponseEntity<>(new ErrorModel(returnStatusCode, errorMessage), returnStatusCode);
    }
}
