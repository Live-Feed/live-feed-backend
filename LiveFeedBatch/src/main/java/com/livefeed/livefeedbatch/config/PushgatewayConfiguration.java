package com.livefeed.livefeedbatch.config;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile("local")
@Configuration
@Slf4j
public class PushgatewayConfiguration {

    @Value("${prometheus.job.name}")
    private String prometheusJobName;

    @Value("${prometheus.grouping.key}")
    private String prometheusGroupingKey;

    @Value("${prometheus.pushgateway.url}")
    private String prometheusPushGatewayUrl;

    private Map<String, String> groupingKey = new HashMap<>();

    private PushGateway pushGateway;

    private CollectorRegistry collectorRegistry;

    @PostConstruct
    public void init() {
        log.info("pushgateway post construct");
        pushGateway = new PushGateway(prometheusPushGatewayUrl);
        groupingKey.put(prometheusGroupingKey, prometheusJobName);
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        collectorRegistry = prometheusMeterRegistry.getPrometheusRegistry();
        Metrics.globalRegistry.add(prometheusMeterRegistry);
    }

//    @Scheduled(fixedRateString = "5000")
    public void pushMetrics() {
        try {
            pushGateway.pushAdd(collectorRegistry, prometheusJobName, groupingKey);
            log.info("push test");
        }
        catch (Throwable ex) {
            log.error("Unable to push metrics to Prometheus Push Gateway", ex);
        }
    }
}
