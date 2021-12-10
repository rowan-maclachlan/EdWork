package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.apigateway.dto.GetShopRequest;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.services.IdGenerator;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.MapperService;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;

public class GetShopRequestHandler extends ApiGatewayRequestHandler<GetShopRequest, CoffeeShopResponse> {

    @Inject
    protected final ShopStore shopStore;
    @Inject
    protected final ModelMapper modelMapper;
    @Inject
    protected final IdGenerator idGenerator;

    public GetShopRequestHandler(
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
    public CoffeeShopResponse handle(GetShopRequest request) throws Exception {

        CoffeeShop newShop = modelMapper.map(request, CoffeeShop.class);
        String newShopId = idGenerator.generateId();
        newShop.setId(newShopId);

        shopStore.addShop(newShop);

        return modelMapper.map(newShop, CoffeeShopResponse.class);
    }

    @Override
    public GetShopRequest parse(String body) throws Exception {
        return mapperService.fromString(GetShopRequest.class, body);
    }

}
