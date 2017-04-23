package com.bachkovsky.rates.xml;

import com.bachkovsky.rates.entity.CurrencyRate;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class XStreamCbrCurrencyRateParserTest {

    private XStreamCbrCurrencyRateParser parser;

    @Before
    public void setUp() {
        parser = new XStreamCbrCurrencyRateParser();
    }

    @Test
    public void testParse() throws Exception {
        List<CurrencyRate> rates = parser.parse(FileUtils.readFileToString(
                FileUtils.getFile("src", "test", "resources", "rates.xml")));
        assertNotNull(rates);
        assertEquals(35, rates.size());
        Optional<CurrencyRate> usdRate = rates.stream()
                                              .filter(rate -> "USD".equals(rate.getCode()))
                                              .findFirst();
        assertTrue(usdRate.isPresent());
        assertEquals("30,7194", usdRate.get().getRate());
        assertEquals(LocalDate.of(2012, 12, 22), usdRate.get().getDate());
    }

    @Test
    public void testParseEmptyRates() throws Exception {
        List<CurrencyRate> rates = parser.parse(FileUtils.readFileToString(
                FileUtils.getFile("src", "test", "resources", "emptyRates.xml")));
        assertNotNull(rates);
        assertEquals(0, rates.size());
    }
}