package com.bachkovsky.rates.xml;

import com.bachkovsky.rates.entity.CurrencyRate;

import javax.validation.constraints.NotNull;
import java.util.List;

@FunctionalInterface
public interface CbrXmlParser {

    @NotNull
    List<CurrencyRate> parse(@NotNull String source);
}
