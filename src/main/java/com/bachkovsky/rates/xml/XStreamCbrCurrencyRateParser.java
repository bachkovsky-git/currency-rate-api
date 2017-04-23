package com.bachkovsky.rates.xml;

import com.bachkovsky.rates.entity.CurrencyRate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class XStreamCbrCurrencyRateParser implements CbrXmlParser {

    private static final DateTimeFormatter CBR_XML_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final XStream xStream;

    public XStreamCbrCurrencyRateParser() {
        xStream = new XStream();
        xStream.alias("ValCurs", CBRCurrencyRates.class);
        xStream.aliasField("Date", CBRCurrencyRates.class, "date");
        xStream.aliasAttribute(CBRCurrencyRates.class, "date", "date");
        xStream.registerConverter(new SingleValueConverter() {
            @Override
            public String toString(Object obj) {
                return CBR_XML_DATE_FORMATTER.format((TemporalAccessor) obj);
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
        xStream.addImplicitCollection(CBRCurrencyRates.class, "currencyRates");

        xStream.alias("Valute", CBRCurrencyRate.class);
        xStream.aliasField("NumCode", CBRCurrencyRate.class, "numericCode");
        xStream.aliasField("CharCode", CBRCurrencyRate.class, "charCode");
        xStream.aliasField("Nominal", CBRCurrencyRate.class, "nominal");
        xStream.aliasField("Name", CBRCurrencyRate.class, "name");
        xStream.aliasField("Value", CBRCurrencyRate.class, "rate");
    }

    @Override
    public List<CurrencyRate> parse(String sourceString) {
        CBRCurrencyRates cbrCurrencyRates = (CBRCurrencyRates) xStream.fromXML(sourceString);
        LocalDate date = cbrCurrencyRates.getDate();
        List<CBRCurrencyRate> currencyRates = cbrCurrencyRates.getCurrencyRates();
        return currencyRates == null ? emptyList() : transformCbrRates(date, currencyRates);
    }

    private static List<CurrencyRate> transformCbrRates(LocalDate date, List<CBRCurrencyRate> currencyRates) {
        return currencyRates.stream()
                            .map(cbrRate -> new CurrencyRate(cbrRate.getCharCode(), cbrRate.getNominal(), cbrRate.getRate(), date))
                            .collect(Collectors.toList());
    }
}