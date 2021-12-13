package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CreateShopRequest;
import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.services.CoffeeValidator;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.MapperService;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.Map;

public class CreateShopRequestHandler extends ApiGatewayRequestHandler<CreateShopRequest, CoffeeShopResponse> {

    @Inject
    protected ShopStore shopStore;
    @Inject
    protected ModelMapper modelMapper;

    public CreateShopRequestHandler() {
        super(DaggerShopComponent.create().loggingService(),
                DaggerShopComponent.create().mapperService(),
                DaggerShopComponent.create().validator());
        this.shopStore = DaggerShopComponent.create().shopStore();
        this.modelMapper = DaggerShopComponent.create().modelMapper();
    }

    public CreateShopRequestHandler(
            LoggingService loggingService,
            MapperService mapperService,
            CoffeeValidator validator,
            ShopStore shopStore,
            ModelMapper modelMapper)
    {
        super(loggingService, mapperService, validator);
        this.shopStore= shopStore;
        this.modelMapper = modelMapper;
    }

    @Override
    public CoffeeShopResponse handle(
            CreateShopRequest request, Map<String, String> queryStringParameters, Map<String, String> pathParameters) throws Exception {
        loggingService.info("Handling request: " + mapperService.toString(request));

        CoffeeShop newShop = modelMapper.map(request, CoffeeShop.class);

        loggingService.info("Saving coffee shop: " + mapperService.toString(newShop));
        shopStore.addShop(newShop);

        return modelMapper.map(newShop, CoffeeShopResponse.class);
    }

    @Override
    public CreateShopRequest parse(String body) throws Exception {
        return mapperService.fromString(CreateShopRequest.class, body);
    }

    @Override
    public void validate(CreateShopRequest request, Map<String, String> queryStringParameters, Map<String, String> pathParameters) {
        validator.validate(request);
    }


}
