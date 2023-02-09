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


    MeterRegistry meterRegistry;
    CircuitBreakerRegistry circuitBreakerRegistry;

    public RESTCallProtector() {
        logger.debug("Creating REST Client Protector");

        meterRegistry = new SimpleMeterRegistry();


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

    public Supplier<RestClientResponse> ProtectService(Supplier<RestClientResponse> supplier){



        logger.debug("*** Protecting Service... ");
        // Create a CircuitBreaker with default configuration

        metrics = circuitBreaker.getMetrics();

        Supplier<RestClientResponse> restClientResponseSupplier = circuitBreaker.decorateSupplier(supplier);


        return restClientResponseSupplier;


    }

    public void addCircuitBreaker(CircuitBreakerConfig circuitBreakerConfig){

        logger.debug("*** Adding CircuitBraker ... ");
        logger.debug("*** Configuration... " + circuitBreakerConfig);

        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        circuitBreakerRegistry.addConfiguration("Custom",circuitBreakerConfig);


        //Use instead of above line to add Circuit breaker to RRegistry
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendService","Custom");

        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);

    }


}
