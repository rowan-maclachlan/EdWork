package org.edwork.goodcoffee.apigateway;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.edwork.goodcoffee.apigateway.dto.CreateShopRequest;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.services.MapperService;
import org.geojson.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateShopRequestHandlerTest {

    MapperService mapper;
    CreateShopRequestHandler handler;

    @BeforeEach
    public void setup() {
        mapper = DaggerShopComponent.create().mapperService();
        handler = new CreateShopRequestHandler(
                DaggerShopComponent.create().loggingService(),
                DaggerShopComponent.create().mapperService(),
                DaggerShopComponent.create().validator(),
                null,
                DaggerShopComponent.create().modelMapper()
        );
    }

    @Test
    public void handleRequestJson() throws Exception {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

        CreateShopRequest createShopRequest = new CreateShopRequest();
        createShopRequest.setRating(5);
        createShopRequest.setName("Kafkas");
        createShopRequest.setLocation(new Point(1, 1));

        String requestBody = mapper.toString(createShopRequest);

        System.out.println(requestBody);
    }
}