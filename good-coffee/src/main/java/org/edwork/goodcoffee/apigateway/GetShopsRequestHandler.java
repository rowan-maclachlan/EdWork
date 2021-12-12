package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.apigateway.dto.CoffeeShopsResponse;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.edwork.goodcoffee.config.ShopConstants;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.exceptions.GoodCoffeeException;
import org.edwork.goodcoffee.services.CoffeeValidator;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.MapperService;
import org.geojson.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.opengis.referencing.operation.TransformException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetShopsRequestHandler extends ApiGatewayRequestHandler<Void, CoffeeShopsResponse> {
    private static final String QUERY_PARAM_LAT = "lat";
    private static final String QUERY_PARAM_LON = "lon";
    private static final String QUERY_PARAM_RATING = "rating";

    @Inject
    protected ShopStore shopStore;
    @Inject
    protected ModelMapper modelMapper;

    public GetShopsRequestHandler() {
        super(DaggerShopComponent.create().loggingService(),
                DaggerShopComponent.create().mapperService(),
                DaggerShopComponent.create().validator());
        this.shopStore = DaggerShopComponent.create().shopStore();
        this.modelMapper = DaggerShopComponent.create().modelMapper();
    }

    public GetShopsRequestHandler(
            LoggingService loggingService,
            MapperService mapperService,
            ShopStore shopStore,
            ModelMapper modelMapper,
            CoffeeValidator validator)
    {
        super(loggingService, mapperService, validator);
        this.shopStore = shopStore;
        this.modelMapper = modelMapper;
    }

    @Override
    public CoffeeShopsResponse handle(Void request, Map<String, String> queryStringParameters) throws TransformException {

        int rating = queryStringParameters.containsKey(QUERY_PARAM_RATING)
                ? Integer.parseInt(queryStringParameters.get(QUERY_PARAM_RATING))
                : ShopConstants.SHOP_RATING_MIN;
        boolean sortShopByDistance = queryStringParameters.containsKey(QUERY_PARAM_LAT)
                && queryStringParameters.containsKey(QUERY_PARAM_LON);

        Stream<CoffeeShop> coffeeShops = shopStore.getShops(rating);
        List<CoffeeShop> shops;
        if (sortShopByDistance) {
            shops = sortShopsByDistance(queryStringParameters, coffeeShops);
        } else {
            shops = coffeeShops.collect(Collectors.toList());
        }

        CoffeeShopsResponse response = new CoffeeShopsResponse();
        response.setCoffeeShops(shops.stream().map(shop -> modelMapper.map(shop, CoffeeShopResponse.class)).collect(Collectors.toList()));
        return response;
    }

    private List<CoffeeShop> sortShopsByDistance(Map<String, String> queryStringParameters, Stream<CoffeeShop> coffeeShops) {
        float lat = Float.parseFloat(queryStringParameters.get(QUERY_PARAM_LAT));
        float lon = Float.parseFloat(queryStringParameters.get(QUERY_PARAM_LON));
        Point start = new Point(lon, lat);
        return coffeeShops.sorted(
                (shop1, shop2) -> compareDistance(getPointDistance(start, shop1.getLocation()), getPointDistance(start, shop2.getLocation())))
                .collect(Collectors.toList());
    }

    private int compareDistance(double d1, double d2)
    {
        double ERR = 0.0001;
        double delta = d1 - d2;
        if(delta > ERR) return 1;
        if(delta < -ERR) return -1;
        return 0;
    }

    private double getPointDistance(Point start, Point end)  {
        // handle antimeridian by shifting negative values into > 180 positive values
        double shiftFactor = 180;
        double startX = start.getCoordinates().getLatitude();
        if (startX < 0) {
            start = new Point(shiftFactor + (shiftFactor - startX), start.getCoordinates().getLongitude());
        }
        double endX = end.getCoordinates().getLatitude();
        if (endX < 0) {
            end = new Point(shiftFactor + (shiftFactor - endX), end.getCoordinates().getLongitude());
        }

        return convert(start).distance(convert(end));
    }

    private org.locationtech.jts.geom.Point convert(Point p) {
        double x = p.getCoordinates().getLatitude();
        double y = p.getCoordinates().getLongitude();
        return new GeometryFactory().createPoint(new CoordinateXY(x, y));
    }

    @Override
    public Void parse(String body) throws Exception {
        // No request body
        return null;
    }

    @Override
    public void validate(Void unused, Map<String, String> queryStringParameters) {
        boolean lat = queryStringParameters.containsKey(QUERY_PARAM_LAT);
        boolean lon = queryStringParameters.containsKey(QUERY_PARAM_LON);
        boolean xor = (lat && lon) || !(lat || lon);
        if (!xor) {
            throw new GoodCoffeeException("Query params must include both lat and lon or neither");
        }
    }

}
