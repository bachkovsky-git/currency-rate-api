package com.bachkovsky.rates.xml;

class CBRCurrencyRate {

    private final Integer numericCode;

    private final String charCode;

    private final Integer nominal;

    private final String name;

    private final String rate;

    CBRCurrencyRate(Integer numericCode, String charCode, Integer nominal, String name, String rate) {
        this.numericCode = numericCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.rate = rate;
    }

    Integer getNumericCode() {
        return numericCode;
    }

    String getCharCode() {
        return charCode;
    }

    Integer getNominal() {
        return nominal;
    }

    String getName() {
        return name;
    }

    String getRate() {
        return rate;
    }
}
