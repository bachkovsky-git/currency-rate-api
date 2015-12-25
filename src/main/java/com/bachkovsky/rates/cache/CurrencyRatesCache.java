package com.bachkovsky.rates.cache;

import com.bachkovsky.rates.dao.CurrencyRatesRepository;
import com.bachkovsky.rates.entity.CurrencyRate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CurrencyRatesCache implements CurrencyRatesRepository {

    private final Cache<CurrencyRatesKey, CurrencyRate> cache;

    private final CurrencyRatesRepository ratesRepository;

    public CurrencyRatesCache(CurrencyRatesRepository repository, long expirationTime, long maxCacheSize) {
        ratesRepository = repository;
        cache = CacheBuilder.<CurrencyRatesKey, CurrencyRate>newBuilder()
                .expireAfterWrite(expirationTime, TimeUnit.MINUTES)
                .maximumSize(maxCacheSize)
                .build();
    }

    @Override
    public Optional<CurrencyRate> getCurrencyRate(String code, LocalDate date) {
        CurrencyRatesKey key = new CurrencyRatesKey(code, date);
        CurrencyRate currencyRate = cache.getIfPresent(key);
        if (currencyRate == null) {
            loadRatesForDate(date);
        }
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    @Override
    public List<CurrencyRate> getCurrencyRates(LocalDate dateTime) {
        Optional<CurrencyRatesKey> key = cache.asMap().keySet().stream()
                .filter(currencyRatesKey -> currencyRatesKey.date.equals(dateTime))
                .findAny();
        if (!key.isPresent()) {
            loadRatesForDate(dateTime);
        }
        return cache.asMap()
                .values()
                .stream()
                .filter(e -> e.getDate().equals(dateTime))
                .collect(Collectors.toList());
    }

    private void loadRatesForDate(LocalDate date) {
        List<CurrencyRate> currencyRates = ratesRepository.getCurrencyRates(date);
        cache.putAll(currencyRates.stream().collect(
                Collectors.toMap(r -> new CurrencyRatesKey(r.getCode(), r.getDate()), Function.identity())));
    }

    private static class CurrencyRatesKey {
        public final String code;
        public final LocalDate date;

        private CurrencyRatesKey(String code, LocalDate date) {
            this.date = date;
            this.code = code;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CurrencyRatesKey that = (CurrencyRatesKey) o;
            return Objects.equals(code, that.code) &&
                    Objects.equals(date, that.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, date);
        }
    }
}