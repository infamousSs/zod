package com.infamous.framework.http;

import com.infamous.framework.factory.JacksonConverterFactory;
import com.infamous.framework.http.engine.ApacheHttpEngine;
import com.infamous.framework.http.engine.JavaHttpEngine;
import com.infamous.framework.http.factory.ZodHttpClientFactory;

public class Demo {

    private static final String BASE_URL = "https://api.github.com";

    public static void main(String[] args) {
        long apache = useApache();
        long java = useJava();

        System.out.println(apache);
        System.out.println(java);
    }

    private static long useApache() {
        ZodHttpClientFactory clientFactory =
            ZodHttpClientFactory.builder()
                .baseUrl(BASE_URL)
                .converterFactory(JacksonConverterFactory.create())
                .callEngine(ApacheHttpEngine.getInstance())
                .config(new HttpConfig())
                .build();

        GithubRestClient client = clientFactory.create(GithubRestClient.class);
        long start = System.nanoTime();
        System.out.println(client.listUsers());
        System.out.println(client.listUsersAsync().join());
        System.out.println(client.user("infamousSs"));
        System.out.println(client.findRepo("java").join());

        return System.nanoTime() - start;
    }

    private static long useJava() {
        ZodHttpClientFactory clientFactory =
            ZodHttpClientFactory.builder()
                .baseUrl(BASE_URL)
                .converterFactory(JacksonConverterFactory.create())
                .callEngine(JavaHttpEngine.getInstance())
                .config(new HttpConfig())
                .build();

        GithubRestClient client = clientFactory.create(GithubRestClient.class);
        long start = System.nanoTime();
        System.out.println(client.listUsers());
        System.out.println(client.listUsersAsync().join());
        System.out.println(client.user("infamousSs"));
        System.out.println(client.findRepo("java").join());

        return System.nanoTime() - start;
    }
}

