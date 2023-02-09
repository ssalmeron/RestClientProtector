package com.evergent.client.serviceprotector;

import com.evergent.client.RestClientResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


public class ServiceCallProtector {
    private static final Logger logger = LogManager.getLogger("AppLogger");

    private CircuitBreaker withCircuitBraker;
    private Supplier<RestClientResponse> thisService;

    ServiceCallProtector(CircuitBreaker withCircuitBraker, Supplier<RestClientResponse> thisService) {
        this.withCircuitBraker = withCircuitBraker;
        this.thisService = thisService;
    }

    public static ServiceCallProtectorBuilder builder() {
        return new ServiceCallProtectorBuilder();
    }


    public static class ServiceCallProtectorBuilder {
        private CircuitBreaker withCircuitBraker;
        private Supplier<RestClientResponse> thisService;

        ServiceCallProtectorBuilder() {
        }

        public ServiceCallProtectorBuilder withCircuitBraker(CircuitBreaker withCircuitBraker) {
            this.withCircuitBraker = withCircuitBraker;
            return this;
        }

        public ServiceCallProtectorBuilder thisService(Supplier<RestClientResponse> thisService) {
            this.thisService = thisService;
            return this;
        }

        public ServiceCallProtector build() {
            return new ServiceCallProtector(withCircuitBraker, thisService);
        }

        public String toString() {
            return "ServiceCallProtector.ServiceCallProtectorBuilder(withCircuitBraker=" + this.withCircuitBraker + ", thisService=" + this.thisService + ")";
        }
    }
}
