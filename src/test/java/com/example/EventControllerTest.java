package com.example;
import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.example.exceptions.ApiErrorMessage;
import com.example.models.EventSaved;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.micronaut.function.aws.proxy.MicronautLambdaHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EventControllerTest {

    private static final Context lambdaContext = new MockLambdaContext();
    private static MicronautLambdaHandler handler;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setupSpec() {
        try {
            handler = new MicronautLambdaHandler();
            objectMapper = handler.getApplicationContext().getBean(ObjectMapper.class);

        } catch (ContainerInitializationException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void cleanupSpec() {
        handler.getApplicationContext().close();
    }

    private String loadTestInput(String name) {
        return new Scanner(EventControllerTest.class.getResourceAsStream("/"+name), StandardCharsets.UTF_8)
                .useDelimiter("\\A")
                .next();
    }

    private AwsProxyResponse post(String json) {
        AwsProxyRequest request = new AwsProxyRequestBuilder("/events", HttpMethod.POST.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(json)
                .build();

        return handler.handleRequest(request, lambdaContext);
    }

    private AwsProxyResponse put(String json, String id) {
        AwsProxyRequest request = new AwsProxyRequestBuilder("/events/"+id, HttpMethod.PUT.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(json)
                .build();

        return handler.handleRequest(request, lambdaContext);
    }

    private AwsProxyResponse get() {
        AwsProxyRequest request = new AwsProxyRequestBuilder("/events", HttpMethod.GET.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();

        return handler.handleRequest(request, lambdaContext);
    }

    private AwsProxyResponse get(String id) {
        AwsProxyRequest request = new AwsProxyRequestBuilder("/events/"+id, HttpMethod.GET.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();

        return handler.handleRequest(request, lambdaContext);
    }

    @Test
    void testPostInvalidEventType() throws Exception {
        // Arrange
        String testInput = new Scanner(EventControllerTest.class.getResourceAsStream("/post-body-1.json"), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        System.out.println(testInput);

        // Act
        AwsProxyResponse response = post(testInput);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        ApiErrorMessage errResponse = objectMapper.readValue(response.getBody(), ApiErrorMessage.class);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode());
        Assertions.assertNotNull(errResponse);
        Assertions.assertNotNull(errResponse.getMessage());
    }

    @Test
    void testPostInvalidDate() throws IOException {
        // Arrange
        String testInput = loadTestInput("post-body-2.json");
        System.out.println(testInput);

        // Act
        AwsProxyResponse response = post(testInput);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        ApiErrorMessage errResponse = objectMapper.readValue(response.getBody(), ApiErrorMessage.class);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode());
        Assertions.assertNotNull(errResponse);
        Assertions.assertNotNull(errResponse.getMessage());
    }

    @Test
    public void testPostMissingSubType() throws Exception {
        // Arrange
        String testInput = loadTestInput("post-body-3.json");
        System.out.println(testInput);

        // Act
        AwsProxyResponse response = post(testInput);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        ApiErrorMessage errResponse = objectMapper.readValue(response.getBody(), ApiErrorMessage.class);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode());
        Assertions.assertNotNull(errResponse);
        Assertions.assertNotNull(errResponse.getMessage());
    }

    @Test
    public void postSaveEvent() throws Exception {
        // Arrange
        String testInput = loadTestInput("post-body-4.json");
        System.out.println(testInput);

        // Act
        AwsProxyResponse response = post(testInput);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        EventSaved eventSaved = objectMapper.readValue(response.getBody(), EventSaved.class);

        // Assert
        Assertions.assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        Assertions.assertNotNull(eventSaved);
        Assertions.assertNotNull(eventSaved.getEventId());
    }
    
    @Test
    public void getInvalidEventId() throws Exception {
        // Act
        AwsProxyResponse response = get("invalid-id");
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        ApiErrorMessage errorMessage = objectMapper.readValue(response.getBody(), ApiErrorMessage.class);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode());
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNotNull(errorMessage.getMessage());
    }
}
