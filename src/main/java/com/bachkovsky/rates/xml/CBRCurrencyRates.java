package com.bachkovsky.rates.xml;

import java.time.LocalDate;
import java.util.List;

class CbrCurrencyRates {

    private final LocalDate date;

    private final List<CbrCurrencyRate> currencyRates;

    public CbrCurrencyRates(List<CbrCurrencyRate> currencyRates, LocalDate date) {
        this.date = date;
        this.currencyRates = currencyRates;
    }

    public List<CbrCurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    public LocalDate getDate() {
        return date;
    }
}
