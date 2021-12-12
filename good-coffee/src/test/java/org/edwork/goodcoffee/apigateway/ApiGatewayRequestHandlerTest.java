package org.edwork.goodcoffee.apigateway;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import jakarta.validation.ValidationException;
import org.apache.http.HttpStatus;
import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.apigateway.dto.CreateShopRequest;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.exceptions.GoodCoffeeException;
import org.edwork.goodcoffee.services.*;
import org.geojson.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;

class ApiGatewayRequestHandlerTest {

    @Mock LoggingService loggingService;
    @Mock MapperService mapperService;
    @Mock ShopStore shopStore;
    @Mock ModelMapper modelMapper;
    @Mock CoffeeValidator validator;

    MapperService realMapperService;

    ApiGatewayRequestHandler<CreateShopRequest, CoffeeShopResponse> handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new TestHandler(loggingService, mapperService, validator, shopStore, modelMapper);
        realMapperService = DaggerShopComponent.create().mapperService();

    }

    @Test
    public void testHandleRequestHandlesParseFailure() throws Exception {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        String testInput = "test string";
        request.setBody(testInput);
        request.setQueryStringParameters(Map.of("input", "output"));

        Mockito.when(mapperService.fromString(CreateShopRequest.class, testInput))
                .thenThrow(new ValidationException("parse error"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleRequestHandlesValidationFailure() throws Exception {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        CreateShopRequest createShopRequest = new CreateShopRequest();
        createShopRequest.setLocation(new Point(1, 1));
        createShopRequest.setName("Kafkas");
        createShopRequest.setRating(5);
        request.setBody(realMapperService.toString(createShopRequest));
        request.setQueryStringParameters(Map.of("input", "output"));

        Mockito.doThrow(new ValidationException())
                .when(validator).validate(any(CreateShopRequest.class));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleRequestHandlesExecutionFailure() throws Exception {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        CreateShopRequest createShopRequest = new CreateShopRequest();
        createShopRequest.setLocation(new Point(1, 1));
        createShopRequest.setName("Kafkas");
        createShopRequest.setRating(5);
        request.setBody(realMapperService.toString(createShopRequest));
        request.setQueryStringParameters(Map.of("input", "output"));

        Mockito.doThrow(new GoodCoffeeException("test exception"))
                .when(shopStore).getShop("Kafkas");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private class TestHandler extends ApiGatewayRequestHandler<CreateShopRequest, CoffeeShopResponse> {

        private final ShopStore shopStore;
        private final ModelMapper modelMapper;

        public TestHandler(
                LoggingService loggingService, MapperService mapperService, CoffeeValidator validator, ShopStore shopStore, ModelMapper modelMapper) {
            super(loggingService, mapperService, validator);
            this.shopStore = shopStore;
            this.modelMapper = modelMapper;
        }

        @Override
        public CoffeeShopResponse handle(CreateShopRequest s, Map<String, String> queryStringParameters) throws Exception {
            return modelMapper.map(shopStore.getShop(s.getName()), CoffeeShopResponse.class);
        }

        @Override
        public CreateShopRequest parse(String body) throws Exception {
            return mapperService.fromString(CreateShopRequest.class, body);
        }

        @Override
        public void validate(CreateShopRequest request, Map<String, String> queryStringParameters) {
            validator.validate(request);
        }
    }
}