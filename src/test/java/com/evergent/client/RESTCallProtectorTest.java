package com.evergent.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class RESTCallProtectorTest {

    private static final Logger logger = LogManager.getLogger("AppLogger");
    @Test
    void addCircuitBreaker() {

        //Create Rest Client
        RestClient client = new RestClient();
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();

        headers.put("some-header","true");
        headers.put("other-header","false");


        RESTCallProtector restCallProtector = new RESTCallProtector();

        RandomNumber randomNumberGenerator = new RandomNumber();


        for (int i = 0; i < 1000; i++) {

            int randomNumber = randomNumberGenerator.getRandomInt(50,100);


            int finalI = i;
            Supplier<RestClientResponse> restClientResponseSupplier = restCallProtector.addCircuitBreaker(50, 10, () -> {
                return client.callService("http://localhost:8080/FaultyRESTService/rest/api/faulty", "service", "Iteration Message " + finalI, headers, randomNumber);
            });

            Try<RestClientResponse> serviceResponse = Try.ofSupplier(restClientResponseSupplier);

            CircuitBreaker.Metrics metrics = restCallProtector.getMetrics();

            logger.debug(" Call " + i + " *** " + "Random: " + randomNumber + " Response: " + serviceResponse.toString());

            logger.debug("-- Metrics");
            logger.debug("Failure Rate: " + metrics.getFailureRate());
            logger.debug("Failed Calls: " + metrics.getNumberOfFailedCalls());
            logger.debug("Successful Calls: " + metrics.getNumberOfSuccessfulCalls());
            logger.debug("CircuitBreaker State: " + restCallProtector.getCircuitBreaker().getState());

        }





    }
}