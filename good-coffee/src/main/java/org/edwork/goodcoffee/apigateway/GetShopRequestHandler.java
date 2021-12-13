package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.exceptions.GoodCoffeeException;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;

public class GetShopRequestHandler extends ApiGatewayRequestHandler<Void, CoffeeShopResponse> {

    private final static String NAME_PATH_PARAM = "name";
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
    }

    @Override
    public CoffeeShopResponse handle(
            Void request, Map<String, String> queryStringParameters, Map<String, String> pathParameters) throws Exception {
        String name = pathParameters.get(NAME_PATH_PARAM);

        CoffeeShop shop = shopStore.getShop(name);
        if (Objects.isNull(shop)) {
            throw new GoodCoffeeException("Resource not found");
        }

        return modelMapper.map(shop, CoffeeShopResponse.class);
    }

    @Override
    public Void parse(String body) throws Exception {
        // pass
        return null;
    }

    @Override
    public void validate(Void getShopRequest, Map<String, String> queryStringParameters, Map<String, String> pathParameters) {
        // pass
    }

}
