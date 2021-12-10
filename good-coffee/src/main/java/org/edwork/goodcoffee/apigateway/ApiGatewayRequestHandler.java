package org.edwork.goodcoffee.apigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.http.HttpStatus;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.MapperService;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public abstract class ApiGatewayRequestHandler<V, T> implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    protected final LoggingService loggingService;

    @Inject
    protected final MapperService mapperService;


    public ApiGatewayRequestHandler(
            LoggingService loggingService,
            MapperService mapperService)
    {
        this.loggingService = loggingService;
        this.mapperService = mapperService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        V v = null;

        try {
            v = parse(request.getBody());
        } catch (Exception e) {
            loggingService.err("Failed to parse request body", e);
            return generateResponse(e.toString(), HttpStatus.SC_BAD_REQUEST);
        }

        String responseBody = null;
        try {
            T t = handle(v);
            responseBody = mapperService.toString(t);
        } catch (Exception e) {
            loggingService.err("Failed to handle request", e);
            return generateResponse(e.toString(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        return generateResponse(responseBody, HttpStatus.SC_CREATED);

    }

    @NotNull
    private APIGatewayProxyResponseEvent generateResponse(String responseBody, int responseCode) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody(responseBody);
        response.setStatusCode(responseCode);
        return response;
    }

    public abstract T handle(V v) throws Exception;

    public abstract V parse(String body) throws Exception;

}
