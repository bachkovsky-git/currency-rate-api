package com.bachkovsky.rates.controller;


import com.bachkovsky.rates.dao.CurrencyRatesRepository;
import com.bachkovsky.rates.entity.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@RestController
public class CurrencyRateController {

    private static final Pattern CURRENCY_CODE_PATTERN = Pattern.compile("[A-Z]{3}");

    private final CurrencyRatesRepository ratesRepository;

    @Autowired
    public CurrencyRateController(CurrencyRatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    @RequestMapping("/api/rate/{code}/{date}")
    public CurrencyRate getRate(
            @PathVariable("code") String code,
            @PathVariable("date") String date) {

        validateCode(code);
        LocalDate localDate = validateDate(date);

        return localDate.isAfter(LocalDate.now()) ? getRate(code) : provideRate(code, localDate);
    }

    @RequestMapping("/api/rate/{code}")
    public CurrencyRate getRate(@PathVariable("code") String code) {
        validateCode(code);
        return provideRate(code, tomorrow());
    }

    private CurrencyRate provideRate(String code, LocalDate date) {
        return ratesRepository.getCurrencyRate(code, date).orElseThrow(() -> new RateNotFoundException(code, date));
    }

    private static LocalDate tomorrow() {
        return LocalDate.now().plusDays(1);
    }

    private static void validateCode(String code) throws ApiException {
        if (!CURRENCY_CODE_PATTERN.matcher(code).matches()) {
            throw new ApiException("code", code);
        }
    }

    private static LocalDate validateDate(String stringDate) throws ApiException {
        try {
            return LocalDate.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new ApiException("date", stringDate);
        }
    }
}