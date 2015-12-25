package com.bachkovsky.rates;

import com.bachkovsky.rates.cache.CurrencyRatesCache;
import com.bachkovsky.rates.dao.CbrCurrencyRateClient;
import com.bachkovsky.rates.dao.CurrencyRatesRepository;
import com.bachkovsky.rates.utils.ApacheHttpClient;
import com.bachkovsky.rates.xml.XStreamCbrCurrencyRateParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;
import java.io.IOException;

@SpringBootApplication
public class Application {

    @Value("${url:http://www.cbr.ru/scripts/XML_daily.asp}")
    private String requestUrl;

    @Value("${httpClientPoolSize:50}")
    private int httpClientPoolSize;

    @Value("${cache.enabled:false}")
    private boolean cachingEnabled;

    @Value("${cache.expirationTime:5}")
    private long cacheExpirationTime;

    @Value("${cache.maxSize:1000}")
    private long maxCacheSize;

    private ApacheHttpClient httpClient;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PreDestroy
    private void shutdown() {
        httpClient.shutdown();
    }

    @Bean
    public CurrencyRatesRepository getCurrencyRateProvider() throws IOException {
        httpClient = new ApacheHttpClient(httpClientPoolSize);
        CurrencyRatesRepository ratesRepository = new CbrCurrencyRateClient(
                httpClient,
                new XStreamCbrCurrencyRateParser(),
                requestUrl
        );

        if (cachingEnabled) {
            ratesRepository = new CurrencyRatesCache(ratesRepository, cacheExpirationTime, maxCacheSize);
        }

        return ratesRepository;
    }
}