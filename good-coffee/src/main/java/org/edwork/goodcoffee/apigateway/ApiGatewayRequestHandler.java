package org.edwork.goodcoffee.apigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.http.HttpStatus;
import org.edwork.goodcoffee.services.CoffeeValidator;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.MapperService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ApiGatewayRequestHandler<V, T> implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected LoggingService loggingService;

    protected MapperService mapperService;

    protected CoffeeValidator validator;

    public ApiGatewayRequestHandler(LoggingService loggingService, MapperService mapperService, CoffeeValidator validator)
    {
        this.loggingService = loggingService;
        this.mapperService = mapperService;
        this.validator = validator;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        loggingService.info(request.toString());

        V body = null;
        Map<String, String> queryParams = Objects.nonNull(request.getQueryStringParameters())
                ? request.getQueryStringParameters() : new HashMap<>();
        Map<String, String> pathParams = Objects.nonNull(request.getPathParameters())
                ? request.getPathParameters() : new HashMap<>();

        try {
            body = parse(request.getBody());
        } catch (Exception e) {
            loggingService.err("Failed to parse request body", e);
            return generateResponse(e.toString(), HttpStatus.SC_BAD_REQUEST);
        }

        try {
            validate(body, queryParams, pathParams);
        } catch (Exception e) {
            loggingService.err("Failed to validate request", e);
            return generateResponse(e.toString(), HttpStatus.SC_BAD_REQUEST);
        }


        String responseBody = null;
        try {
            T t = handle(body, queryParams, pathParams);
            responseBody = mapperService.toString(t);
        } catch (Exception e) {
            loggingService.err("Failed to handle request", e);
            return generateResponse(e.toString(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        APIGatewayProxyResponseEvent response = generateResponse(responseBody, HttpStatus.SC_CREATED);

        loggingService.info(response.toString());

        return response;

    }

    @NotNull
    private APIGatewayProxyResponseEvent generateResponse(String responseBody, int responseCode) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody(responseBody);
        response.setStatusCode(responseCode);
        return response;
    }

    public abstract T handle(V v, Map<String, String> queryStringParameters, Map<String, String> pathParameters) throws Exception;

    public abstract V parse(String body) throws Exception;

    public abstract void validate(V v, Map<String, String> queryStringParameters, Map<String, String> pathParameters);
}
