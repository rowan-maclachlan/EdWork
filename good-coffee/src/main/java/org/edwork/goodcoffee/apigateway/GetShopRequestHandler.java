package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.apigateway.dto.GetShopRequest;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.services.IdGenerator;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.Map;

public class GetShopRequestHandler extends ApiGatewayRequestHandler<GetShopRequest, CoffeeShopResponse> {

    @Inject
    protected ShopStore shopStore;
    @Inject
    protected ModelMapper modelMapper;

    public GetShopRequestHandler() {
        super(DaggerShopComponent.create().loggingService(),
                DaggerShopComponent.create().mapperService(),
                DaggerShopComponent.create().validator());
        this.shopStore = DaggerShopComponent.create().shopStore();
        this.modelMapper = DaggerShopComponent.create().modelMapper();
        loggingService.info("Created CreateShopRequestHandler");
    }

    @Override
    public CoffeeShopResponse handle(GetShopRequest request, Map<String, String> queryStringParameters) throws Exception {
        loggingService.info("Handling request: " + mapperService.toString(request));
        return modelMapper.map(shopStore.getShop(request.getName()), CoffeeShopResponse.class);
    }

    @Override
    public GetShopRequest parse(String body) throws Exception {
        return mapperService.fromString(GetShopRequest.class, body);
    }

    @Override
    public void validate(GetShopRequest getShopRequest, Map<String, String> queryStringParameters) {
        validator.validate(getShopRequest);
    }

}
