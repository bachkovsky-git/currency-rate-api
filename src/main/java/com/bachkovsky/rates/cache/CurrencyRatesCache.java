package com.bachkovsky.rates.cache;

import com.bachkovsky.rates.dao.CurrencyRatesRepository;
import com.bachkovsky.rates.entity.CurrencyRate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CurrencyRatesCache implements CurrencyRatesRepository {

    private final LoadingCache<LocalDate, Map<String, CurrencyRate>> cache;

    public CurrencyRatesCache(CurrencyRatesRepository repository, long expirationTime, long maxCacheSize) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTime, TimeUnit.MINUTES)
                .maximumSize(maxCacheSize)
                .build(new CacheLoader<LocalDate, Map<String, CurrencyRate>>() {
                    @Override
                    public Map<String, CurrencyRate> load(LocalDate date) throws Exception {
                        return repository.getCurrencyRates(date)
                                         .stream()
                                         .collect(Collectors.toMap(CurrencyRate::getCode, Function.identity()));
                    }
                });
    }

    @Override
    public Optional<CurrencyRate> getCurrencyRate(String code, LocalDate date) {
        return Optional.ofNullable(cache.getUnchecked(date).get(code));
    }

    @Override
    public List<CurrencyRate> getCurrencyRates(LocalDate dateTime) {
        return new ArrayList<>(cache.getUnchecked(dateTime).values());
    }
}