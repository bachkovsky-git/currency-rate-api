package com.bachkovsky.rates.entity;

import com.bachkovsky.rates.controller.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CurrencyRate {

    private final String code;

    private final int nominal;

    private final String rate;

    @JsonSerialize(using = LocalDateSerializer.class)
    private final LocalDate date;

    public CurrencyRate(@NotNull String code, int nominal, @NotNull String rate, @NotNull LocalDate date) {
        this.code = code;
        this.nominal = nominal;
        this.rate = rate;
        this.date = date;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    @NotNull
    public int getNominal() {
        return nominal;
    }

    @NotNull
    public String getRate() {
        return rate;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }
}
