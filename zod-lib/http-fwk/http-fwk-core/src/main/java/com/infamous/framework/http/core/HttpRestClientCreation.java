package com.infamous.framework.http.core;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.factory.ZodHttpClientFactory;

public final class HttpRestClientCreation {

    private final HttpConfig m_config;

    private static HttpRestClientCreation INSTANCE;

    private HttpRestClientCreation(HttpConfig config) {
        m_config = config;
    }

    public synchronized static HttpRestClientCreation getInstance(HttpConfig config) {
        if (INSTANCE == null) {
            INSTANCE = new HttpRestClientCreation(config);
        }
        if (!INSTANCE.getConfig().equals(config)) {
            INSTANCE.shutdown();
            INSTANCE = new HttpRestClientCreation(config);
        }
        return INSTANCE;
    }

    public synchronized static HttpRestClientCreation getInstance(ZodHttpClientFactory zodHttpClientFactory) {
        return getInstance(zodHttpClientFactory.config());
    }

    public HttpConfig getConfig() {
        return m_config;
    }

    public void shutdown() {
        m_config.shutdown();
    }

    public HttpRequestWithBody create(String baseUrl, String url,
                                      HttpMethod method, ObjectMapper objectMapper) {
        return new HttpRequestBody(baseUrl, url, method, objectMapper);
    }

    public HttpRequestWithBody create(ZodHttpClientFactory zodHttpClientFactory, String url, HttpMethod method) {
        return create(zodHttpClientFactory.baseUrl(), url, method, zodHttpClientFactory.objectMapper());
    }
}
