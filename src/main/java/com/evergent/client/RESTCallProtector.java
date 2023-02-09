package com.evergent.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.enterprise.context.Dependent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@Dependent
public class RESTCallProtector  {

    private static final Logger logger = LogManager.getLogger("AppLogger");



    private CircuitBreaker circuitBreaker ;
    private CircuitBreaker.Metrics metrics;
    private CircuitBreakerConfig circuitBreakerConfig;

    MeterRegistry meterRegistry;
    CircuitBreakerRegistry circuitBreakerRegistry;

    public RESTCallProtector() {
        logger.debug("Creating REST Client Protector");

        meterRegistry = new SimpleMeterRegistry();
        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        //circuitBreaker = CircuitBreaker.ofDefaults("backendService");

        //Use instead of above line to add Circuitbreaker to REgistry
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendService");


        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);






    }


    public CircuitBreaker.Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(CircuitBreaker.Metrics metrics) {
        this.metrics = metrics;
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public Supplier<RestClientResponse> addCircuitBreaker(int failureRateThreshold, int minimumNumberOfCalls, Supplier<RestClientResponse> supplier){

        circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRateThreshold)
                .minimumNumberOfCalls(minimumNumberOfCalls)
                .build();

        logger.debug("Adding Circuitbreaker..");
        // Create a CircuitBreaker with default configuration

        metrics = circuitBreaker.getMetrics();

        Supplier<RestClientResponse> restClientResponseSupplier = circuitBreaker.decorateSupplier(supplier);


        return restClientResponseSupplier;


    }


}
