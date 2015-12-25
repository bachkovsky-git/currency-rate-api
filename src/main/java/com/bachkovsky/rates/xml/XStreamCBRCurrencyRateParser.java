package com.bachkovsky.rates.xml;

import com.bachkovsky.rates.entity.CurrencyRate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class XStreamCbrCurrencyRateParser implements CbrXmlParser {

    private static final DateTimeFormatter CBR_XML_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final XStream xStream;

    public XStreamCbrCurrencyRateParser() {
        xStream = new XStream();
        xStream.alias("ValCurs", CbrCurrencyRates.class);
        xStream.aliasField("Date", CbrCurrencyRates.class, "date");
        xStream.aliasAttribute(CbrCurrencyRates.class, "date", "date");
        xStream.registerConverter(new SingleValueConverter() {
            @Override
            public String toString(Object obj) {
                return CBR_XML_DATE_FORMATTER.format((LocalDate) obj);
            }

            @Override
            public Object fromString(String str) {
                return LocalDate.parse(str, CBR_XML_DATE_FORMATTER);
            }

            @Override
            public boolean canConvert(Class type) {
                return LocalDate.class.isAssignableFrom(type);
            }
        });
        xStream.addImplicitCollection(CbrCurrencyRates.class, "currencyRates");

        xStream.alias("Valute", CbrCurrencyRate.class);
        xStream.aliasField("NumCode", CbrCurrencyRate.class, "numericCode");
        xStream.aliasField("CharCode", CbrCurrencyRate.class, "charCode");
        xStream.aliasField("Nominal", CbrCurrencyRate.class, "nominal");
        xStream.aliasField("Name", CbrCurrencyRate.class, "name");
        xStream.aliasField("Value", CbrCurrencyRate.class, "rate");
    }

    @Override
    public List<CurrencyRate> parse(String sourceString) {
        CbrCurrencyRates cbrCurrencyRates = (CbrCurrencyRates) xStream.fromXML(sourceString);
        LocalDate date = cbrCurrencyRates.getDate();
        List<CbrCurrencyRate> currencyRates = cbrCurrencyRates.getCurrencyRates();
        return currencyRates == null ? Collections.emptyList() : currencyRates
                .stream()
                .map(cbrRate -> new CurrencyRate(cbrRate.getCharCode(), cbrRate.getNominal(), cbrRate.getRate(), date))
                .collect(Collectors.toList());
    }
}