package com.docapproval.api.common.error;

import com.docapproval.api.response.common.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { UserDataException.class })
    protected ResponseEntity<ErrorModel> handleUserDataException(UserDataException e) {
        return new ResponseEntity<>(new ErrorModel(e.getStatusCode(), e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    protected ResponseEntity<ErrorModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.BAD_REQUEST, e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorModel> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
