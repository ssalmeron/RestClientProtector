package com.evergent.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;

public class RestClient {

    private static final Logger logger = LogManager.getLogger("AppLogger");

    Client client;
    public RestClient() {
        init();
    }

    void init(){
        client = ClientBuilder.newClient().register(RestClientLoggingFilter.class);


    }

    public RestClientResponse callService(String baseURL, String resource, String message, LinkedHashMap<String,String> headers, int randomNumber) throws RestClientGenericError {


        WebTarget target = client.target(baseURL).path(resource).queryParam("message",message).queryParam("randomNumber",randomNumber);;
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

        //Add headers to the InvocationBuilder from the headers LinkedHashMap parameter
        headers.forEach(invocationBuilder::header);

        Response invocationResponse = invocationBuilder.get();

        logger.debug(invocationResponse.getStatus());
        logger.debug(invocationResponse.getStatusInfo());


        FaultyServiceResponse faultyServiceResponse;

        RestClientResponse restClientResponse= new RestClientResponse();


        if(invocationResponse.getStatus()== Response.Status.OK.getStatusCode()){
           faultyServiceResponse = invocationResponse.readEntity(FaultyServiceResponse.class);
            logger.debug(faultyServiceResponse);

            //Add the serviceResponse to the RestClient Response object
            restClientResponse.setCode(String.valueOf(invocationResponse.getStatus()));
            restClientResponse.setResponse(faultyServiceResponse);
        } else {

            throw  new RestClientGenericError( String.valueOf(invocationResponse.getStatus()),String.valueOf(invocationResponse.getStatusInfo()));



        }



        logger.debug(restClientResponse);

        return restClientResponse;
    }


}
