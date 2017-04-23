package com.bachkovsky.rates.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ApiException extends RuntimeException {

    ApiException(String fieldName, String value) {
        super("Incorrect parameter: " + fieldName + "= '" + value + "'");
    }
}