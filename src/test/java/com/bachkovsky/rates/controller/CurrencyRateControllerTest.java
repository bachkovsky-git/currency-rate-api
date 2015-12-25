package com.bachkovsky.rates.controller;

import com.bachkovsky.rates.dao.CurrencyRatesRepository;
import com.bachkovsky.rates.entity.CurrencyRate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CurrencyRateController.class)
public class CurrencyRateControllerTest {

    private MockMvc mockMvc;

    @Before
    public void before() throws Exception {
        CurrencyRatesRepository ratesRepository = mock(CurrencyRatesRepository.class);
        when(ratesRepository.getCurrencyRate("USD", LocalDate.of(2016, 1, 13))).thenReturn(
                Optional.of(new CurrencyRate("USD", 1, "75,215", LocalDate.of(2016, 1, 13))));
        when(ratesRepository.getCurrencyRate("USD", LocalDate.of(2016, 1, 14))).thenReturn(
                Optional.of(new CurrencyRate("USD", 1, "76,215", LocalDate.of(2016, 1, 14))));

        when(ratesRepository.getCurrencyRate("JPY", LocalDate.of(2016, 1, 13))).thenReturn(
                Optional.of(new CurrencyRate("JPY", 100, "62,415", LocalDate.of(2016, 1, 13))));
        when(ratesRepository.getCurrencyRate("JPY", LocalDate.of(2016, 1, 14))).thenReturn(
                Optional.of(new CurrencyRate("JPY", 100, "64,415", LocalDate.of(2016, 1, 14))));

        when(ratesRepository.getCurrencyRate(eq("UAS"), any(LocalDate.class))).thenReturn(Optional.empty());

        when(ratesRepository.getCurrencyRates(LocalDate.of(2016, 1, 14))).thenReturn(
                asList(new CurrencyRate("USD", 1, "76,215", LocalDate.of(2016, 1, 14)),
                        new CurrencyRate("JPY", 100, "64,415", LocalDate.of(2016, 1, 14))));

        when(ratesRepository.getCurrencyRates(LocalDate.of(2016, 1, 15))).thenReturn(emptyList());

        CurrencyRateController currencyRateController = spy(new CurrencyRateController(ratesRepository));
        doReturn(LocalDate.of(2016, 1, 14)).when(currencyRateController, "tomorrow");

        mockMvc = MockMvcBuilders.standaloneSetup(currencyRateController).build();
    }

    @Test
    public void testGetRate() throws Exception {
        mockMvc.perform(get("/api/rate/USD/2016-01-13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("USD")))
                .andExpect(jsonPath("$.nominal", is(1)))
                .andExpect(jsonPath("$.rate", is("75,215")))
                .andExpect(jsonPath("$.date", is("2016-01-13")));
    }

    @Test
    public void testGetRateNominal() throws Exception {
        mockMvc.perform(get("/api/rate/JPY/2016-01-13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("JPY")))
                .andExpect(jsonPath("$.nominal", is(100)))
                .andExpect(jsonPath("$.rate", is("62,415")))
                .andExpect(jsonPath("$.date", is("2016-01-13")));
    }

    @Test
    public void testTomorrowRate() throws Exception {
        mockMvc.perform(get("/api/rate/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("USD")))
                .andExpect(jsonPath("$.nominal", is(1)))
                .andExpect(jsonPath("$.rate", is("76,215")))
                .andExpect(jsonPath("$.date", is("2016-01-14")));
    }

    @Test
    public void testTomorrowRateNominal() throws Exception {
        mockMvc.perform(get("/api/rate/JPY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("JPY")))
                .andExpect(jsonPath("$.nominal", is(100)))
                .andExpect(jsonPath("$.rate", is("64,415")))
                .andExpect(jsonPath("$.date", is("2016-01-14")));
    }

    @Test
    public void testRateNotFound() throws Exception {
        mockMvc.perform(get("/api/rate/UAS/2016-01-13")).andExpect(status().isNotFound());
    }

    @Test
    public void testTomorrowRateNotFound() throws Exception {
        mockMvc.perform(get("/api/rate/UAS")).andExpect(status().isNotFound());
    }

    @Test
    public void testInvalidDate() throws Exception {
        mockMvc.perform(get("/api/rate/USD/s1s.20.1")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/USD/2011.11.1")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/USD/1.1.1.1")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/USD/1-1-1-1")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/USD/2-2")).andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidCode() throws Exception {
        mockMvc.perform(get("/api/rate/usd")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/US")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/Usd")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/rate/USDA")).andExpect(status().isBadRequest());
    }
}