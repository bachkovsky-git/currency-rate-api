package com.bachkovsky.rates.xml;

class CbrCurrencyRate {

    private final Integer numericCode;

    private final String charCode;

    private final Integer nominal;

    private final String name;

    private final String rate;

    public CbrCurrencyRate(Integer numericCode, String charCode, Integer nominal, String name, String rate) {
        this.numericCode = numericCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.rate = rate;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public Integer getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }
}
