package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CreateShopRequest;
import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.services.IdGenerator;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.MapperService;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;

public class CreateShopRequestHandler extends ApiGatewayRequestHandler<CreateShopRequest, CoffeeShopResponse> {

    @Inject
    protected final ShopStore shopStore;
    @Inject
    protected final ModelMapper modelMapper;
    @Inject
    protected final IdGenerator idGenerator;

    public CreateShopRequestHandler(
            LoggingService loggingService,
            ShopStore shopStore,
            ModelMapper modelMapper,
            MapperService mapperService,
            IdGenerator idGenerator) {
        super(loggingService, mapperService);

        this.shopStore = shopStore;
        this.modelMapper = modelMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public CoffeeShopResponse handle(CreateShopRequest request) throws Exception {

        CoffeeShop newShop = modelMapper.map(request, CoffeeShop.class);
        String newShopId = idGenerator.generateId();
        newShop.setId(newShopId);

        shopStore.addShop(newShop);

        return modelMapper.map(newShop, CoffeeShopResponse.class);
    }

    @Override
    public CreateShopRequest parse(String body) throws Exception {
        return mapperService.fromString(CreateShopRequest.class, body);
    }

}
