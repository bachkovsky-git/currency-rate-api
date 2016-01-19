package com.bachkovsky.rates.dao;

import com.bachkovsky.rates.entity.CurrencyRate;
import com.bachkovsky.rates.utils.SimpleHttpClient;
import com.bachkovsky.rates.xml.CbrXmlParser;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonMap;

public class CbrCurrencyRateClient implements CurrencyRatesRepository {

    private static final DateTimeFormatter CBR_REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String DATE_PARAM = "date_req";

    private final SimpleHttpClient httpClient;

    private final CbrXmlParser xmlParser;

    private final String requestUrl;

    public CbrCurrencyRateClient(@NotNull SimpleHttpClient httpClient, @NotNull CbrXmlParser xmlParser,
                                 @NotNull String requestUrl) {
        this.httpClient = httpClient;
        this.xmlParser = xmlParser;
        this.requestUrl = requestUrl;
    }

    @Override
    public Optional<CurrencyRate> getCurrencyRate(String code, LocalDate date) {
        return getCurrencyRates(date)
                .stream()
                .filter(cbrRate -> code.equals(cbrRate.getCode()))
                .findAny();
    }

    @Override
    public List<CurrencyRate> getCurrencyRates(LocalDate dateTime) {
        String response = httpClient.makeRequest(requestUrl,
                singletonMap(DATE_PARAM, CBR_REQUEST_DATE_FORMAT.format(dateTime)));
        return xmlParser.parse(response);
    }
}