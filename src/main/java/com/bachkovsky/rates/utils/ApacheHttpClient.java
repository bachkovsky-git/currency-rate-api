package com.bachkovsky.rates.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;

public class ApacheHttpClient implements SimpleHttpClient {

    private final CloseableHttpClient httpClient;
    private final PoolingHttpClientConnectionManager cm;

    public ApacheHttpClient(Integer httpClientMaxPoolSize) {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(httpClientMaxPoolSize);
        httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .build();
    }

    public void shutdown() {
        cm.shutdown();
    }

    @Override
    public String makeRequest(String url, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            params.forEach(uriBuilder::addParameter);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            try (CloseableHttpResponse res = httpClient.execute(httpGet)) {
                InputStream content = res.getEntity().getContent();
                return IOUtils.toString(content);
            }
        } catch (Exception e) {
            System.err.println(MessageFormat.format("Error occurred while making request to URL: {0}, " +
                    "with params: {1}", url, params));
            e.printStackTrace(System.err);
            return "";
        }
    }
}