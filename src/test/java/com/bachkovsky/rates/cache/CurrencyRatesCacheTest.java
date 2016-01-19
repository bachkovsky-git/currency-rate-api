package com.bachkovsky.rates.cache;

import com.bachkovsky.rates.dao.CurrencyRatesRepository;
import com.bachkovsky.rates.entity.CurrencyRate;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyRatesCacheTest {

    private CurrencyRatesCache cache;

    @Before
    public void setUp() throws Exception {
        CurrencyRatesRepository ratesRepository = mock(CurrencyRatesRepository.class);
        when(ratesRepository.getCurrencyRates(LocalDate.of(2016, 1, 13))).thenReturn(
                asList(new CurrencyRate("USD", 1, "76,215", LocalDate.of(2016, 1, 14)),
                        new CurrencyRate("JPY", 100, "64,415", LocalDate.of(2016, 1, 14))));
        when(ratesRepository.getCurrencyRates(LocalDate.of(2016, 1, 15))).thenReturn(emptyList());

        cache = new CurrencyRatesCache(ratesRepository, 1, 10);
    }

    @Test
    public void testGetCurrencyRate() throws Exception {
        Optional<CurrencyRate> existingRate = cache.getCurrencyRate("USD", LocalDate.of(2016, 1, 13));
        assertTrue(existingRate.isPresent());

        Optional<CurrencyRate> notExistingRate = cache.getCurrencyRate("USD", LocalDate.of(2016, 1, 17));
        assertFalse(notExistingRate.isPresent());
    }

    @Test
    public void testGetCurrencyRates() throws Exception {
        List<CurrencyRate> currencyRates = cache.getCurrencyRates(LocalDate.of(2016, 1, 13));
        assertThat(currencyRates, is(not(empty())));

        currencyRates = cache.getCurrencyRates(LocalDate.of(2016, 1, 15));
        assertThat(currencyRates, is(empty()));
    }
}