package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.DeleteShopResponse;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.Map;

public class DeleteShopRequestHandler extends ApiGatewayRequestHandler<Void, DeleteShopResponse> {

    private final static String NAME_PATH_PARAM = "name";

    @Inject
    protected ShopStore shopStore;
    @Inject
    protected ModelMapper modelMapper;

    public DeleteShopRequestHandler() {
        super(DaggerShopComponent.create().loggingService(),
                DaggerShopComponent.create().mapperService(),
                DaggerShopComponent.create().validator());
        this.shopStore = DaggerShopComponent.create().shopStore();
        this.modelMapper = DaggerShopComponent.create().modelMapper();
    }

    @Override
    public DeleteShopResponse handle(Void unused, Map<String, String> queryStringParameters, Map<String, String> pathParameters) throws Exception {
        String name = pathParameters.get(NAME_PATH_PARAM);
        loggingService.info("Finding shop with name: " + name);
        CoffeeShop shop = shopStore.getShop(name);
        loggingService.info("Deleting shop: " + mapperService.toString(shop));
        shopStore.deleteShop(shop);

        return modelMapper.map(shop, DeleteShopResponse.class);
    }

    @Override
    public Void parse(String body) throws Exception {
        return null;
    }

    @Override
    public void validate(Void unused, Map<String, String> queryStringParameters, Map<String, String> pathParameters) {

    }
}
