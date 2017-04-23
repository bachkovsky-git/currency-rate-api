package com.bachkovsky.rates.xml;

import java.time.LocalDate;
import java.util.List;

class CBRCurrencyRates {

    private final LocalDate date;

    private final List<CBRCurrencyRate> currencyRates;

    CBRCurrencyRates(List<CBRCurrencyRate> currencyRates, LocalDate date) {
        this.date = date;
        this.currencyRates = currencyRates;
    }

    List<CBRCurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    LocalDate getDate() {
        return date;
    }
}
