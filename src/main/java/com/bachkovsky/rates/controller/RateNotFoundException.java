package com.bachkovsky.rates.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.NOT_FOUND)
class RateNotFoundException extends RuntimeException {
    public RateNotFoundException(String code, LocalDate date) {
        super("Could not find rate: code='" + code + "', date='" + date + "'");
    }
}
