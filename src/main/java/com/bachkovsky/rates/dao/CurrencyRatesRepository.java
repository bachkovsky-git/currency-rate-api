package com.bachkovsky.rates.dao;

import com.bachkovsky.rates.entity.CurrencyRate;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CurrencyRatesRepository {

    @NotNull
    Optional<CurrencyRate> getCurrencyRate(String code, LocalDate date);

    @NotNull
    List<CurrencyRate> getCurrencyRates(LocalDate dateTime);
}
