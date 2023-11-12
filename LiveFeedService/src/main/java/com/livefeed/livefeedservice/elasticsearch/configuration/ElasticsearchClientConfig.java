package com.livefeed.livefeedservice.elasticsearch.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticsearchClientConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.config.host-and-port}")
    private String hostAndPort;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .build();
    }
}
