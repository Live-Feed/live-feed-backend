package com.livefeed.livefeedservice.elasticsearch.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchClientConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.config.host-and-port}")
    private String hostAndPort;

    @Value("${elasticsearch.config.user}")
    private String user;

    @Value("${elasticsearch.config.password}")
    private String password;

    private final Environment environment;

    @Override
    public ClientConfiguration clientConfiguration() {
        return createConfig();
    }

    private ClientConfiguration createConfig() {

        String[] activeProfiles = environment.getActiveProfiles();

        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder()
                .connectedTo(hostAndPort);

        if (hasProdProfile(activeProfiles)) {
            builder.withBasicAuth(user, password);
        }

        return builder.build();
    }

    private boolean hasProdProfile(String[] activeProfiles) {
        return List.of(activeProfiles).contains("prod");
    }
}
