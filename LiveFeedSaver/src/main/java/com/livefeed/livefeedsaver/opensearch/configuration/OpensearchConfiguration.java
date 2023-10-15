package com.livefeed.livefeedsaver.opensearch.configuration;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.sniff.Sniffer;
import org.opensearch.data.client.orhlc.ClientConfiguration;
import org.opensearch.spring.boot.autoconfigure.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class OpensearchConfiguration {

    @Profile("local")
    @Bean
    public RestClientBuilderCustomizer customizer() {
        return new RestClientBuilderCustomizer() {
            @Override
            public void customize(RestClientBuilder builder) {
            }

            @Override
            public void customize(HttpAsyncClientBuilder builder) {
                try {
                    builder.setSSLContext(new SSLContextBuilder()
                            .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                            .build());
                } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
                    throw new RuntimeException("Failed to initialize SSL Context instance", ex);
                }
            }
        };
    }

    @Profile("local")
    @Bean
    public Sniffer sniffer() {
        return null;
    }

//    @Bean
//    public ClientConfiguration clientConfiguration() {
//        return ClientConfiguration.builder()
//                .connectedToLocalhost()
//                .withBasicAuth("admin", "admin")
//                .build();
//    }

//    @Bean
//    public RestClientBuilder restClientBuilder() {
//
//    }

}
