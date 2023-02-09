package com.evergent.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rest Client test")
class RestClientTest {

    @Test
    void callService() {
        RestClient client = new RestClient();
        LinkedHashMap<String,String> headers = new LinkedHashMap<>();

        headers.put("some-header","true");
        headers.put("other-header","false");

        RESTCallProtector restCallProtector = new RESTCallProtector();

        RandomNumber randomNumberGenerator = new RandomNumber();

        int randomNumber = randomNumberGenerator.getRandomInt(1,100);

        try {
            client.callService("http://localhost:8080/FaultyRESTService/rest/api/faulty", "service", "New Message", headers,randomNumber);
        } catch (RestClientGenericError e) {
            throw new RuntimeException(e);
        }


    }
}