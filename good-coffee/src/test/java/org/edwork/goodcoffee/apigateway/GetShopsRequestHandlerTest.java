package org.edwork.goodcoffee.apigateway;

import org.edwork.goodcoffee.apigateway.dto.CoffeeShopResponse;
import org.edwork.goodcoffee.apigateway.dto.CoffeeShopsResponse;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.exceptions.GoodCoffeeException;
import org.edwork.goodcoffee.services.CoffeeValidator;
import org.geojson.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.opengis.referencing.operation.TransformException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GetShopsRequestHandlerTest {

    @Mock
    protected ShopStore shopStore;
    @Mock
    protected ModelMapper modelMapper;
    @Mock
    protected CoffeeValidator validator;

    private GetShopsRequestHandler requestHandler;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        requestHandler = new GetShopsRequestHandler(null, null, shopStore, modelMapper, validator);
    }

    @Test
    void handleAFewShops() throws TransformException {

        CoffeeShop nearestShop = createCoffeeShop(createPoint(2, 2), "id1");
        CoffeeShop middleShop = createCoffeeShop(createPoint(3, 3), "id2");
        CoffeeShop farShop = createCoffeeShop(createPoint(4, 4), "id3");
        Mockito.when(shopStore.getShops(Mockito.any(Integer.class)))
                .thenReturn(Stream.of(
                        farShop, middleShop, nearestShop
                ));

        CoffeeShopResponse nearestShopResponse = createShopResponse(createPoint(2, 2), "id1");
        CoffeeShopResponse middleShopResponse = createShopResponse(createPoint(3, 3), "id2");
        CoffeeShopResponse farShopResponse = createShopResponse(createPoint(4, 4), "id3");
        Mockito.when(modelMapper.map(nearestShop, CoffeeShopResponse.class)).thenReturn(nearestShopResponse);
        Mockito.when(modelMapper.map(middleShop, CoffeeShopResponse.class)).thenReturn(middleShopResponse);
        Mockito.when(modelMapper.map(farShop, CoffeeShopResponse.class)).thenReturn(farShopResponse);

        Map<String, String> coords = new HashMap<>();
        coords.put("lat", "1.00");
        coords.put("lon", "1.00");

        CoffeeShopsResponse response = requestHandler.handle(null, coords);

        assertEquals(3, response.getCoffeeShops().size());
        assertEquals(nearestShop.getName(), response.getCoffeeShops().get(0).getName());
        assertEquals(middleShop.getName(), response.getCoffeeShops().get(1).getName());
        assertEquals(farShop.getName(), response.getCoffeeShops().get(2).getName());
    }


    @Test
    void handleAFewShopsNegativeLongitudes() throws TransformException {

        CoffeeShop nearestShop = createCoffeeShop(createPoint(-2, 2), "id1");
        CoffeeShop middleShop = createCoffeeShop(createPoint(-3, 3), "id2");
        CoffeeShop farShop = createCoffeeShop(createPoint(-4, 4), "id3");
        Mockito.when(shopStore.getShops(Mockito.any(Integer.class)))
                .thenReturn(Stream.of(
                        farShop, middleShop, nearestShop
                ));

        CoffeeShopResponse nearestShopResponse = createShopResponse(createPoint(-2, 2), "id1");
        CoffeeShopResponse middleShopResponse = createShopResponse(createPoint(-3, 3), "id2");
        CoffeeShopResponse farShopResponse = createShopResponse(createPoint(-4, 4), "id3");
        Mockito.when(modelMapper.map(nearestShop, CoffeeShopResponse.class)).thenReturn(nearestShopResponse);
        Mockito.when(modelMapper.map(middleShop, CoffeeShopResponse.class)).thenReturn(middleShopResponse);
        Mockito.when(modelMapper.map(farShop, CoffeeShopResponse.class)).thenReturn(farShopResponse);

        Map<String, String> coords = new HashMap<>();
        coords.put("lat", "1.00");
        coords.put("lon", "-1.00");

        CoffeeShopsResponse response = requestHandler.handle(null, coords);

        assertEquals(3, response.getCoffeeShops().size());
        assertEquals(nearestShop.getName(), response.getCoffeeShops().get(0).getName());
        assertEquals(middleShop.getName(), response.getCoffeeShops().get(1).getName());
        assertEquals(farShop.getName(), response.getCoffeeShops().get(2).getName());
    }

    @Test
    void handleAntiMeridian() throws TransformException {

        CoffeeShop crossMeridianShop = createCoffeeShop(createPoint(179, 0), "id1");
        CoffeeShop farShop = createCoffeeShop(createPoint(170, 0), "id2");
        Mockito.when(shopStore.getShops(Mockito.any(Integer.class)))
                .thenReturn(Stream.of(farShop, crossMeridianShop));

        CoffeeShopResponse crossMeridianShopResponse = createShopResponse(createPoint(179, 0), "id1");
        CoffeeShopResponse farShopResponse = createShopResponse(createPoint(170, 0), "id2");
        Mockito.when(modelMapper.map(crossMeridianShop, CoffeeShopResponse.class)).thenReturn(crossMeridianShopResponse);
        Mockito.when(modelMapper.map(farShop, CoffeeShopResponse.class)).thenReturn(farShopResponse);

        Map<String, String> coords = new HashMap<>();
        coords.put("lat", "0");
        coords.put("lon", "-179");

        CoffeeShopsResponse response = requestHandler.handle(null, coords);

        assertEquals(2, response.getCoffeeShops().size());
        assertEquals(crossMeridianShop.getName(), response.getCoffeeShops().get(0).getName());
        assertEquals(farShop.getName(), response.getCoffeeShops().get(1).getName());
    }

    @Test
    void handleNoShops() throws TransformException {

        Map<String, String> coords = new HashMap<>();
        coords.put("lat", "1.00");
        coords.put("lon", "1.00");
        Mockito.when(shopStore.getShops(Mockito.any(Integer.class)))
                .thenReturn(Stream.empty());
        CoffeeShopsResponse response = requestHandler.handle(null, coords);

        assertEquals(0, response.getCoffeeShops().size());
    }

    @Test
    void handleValidationBothLatAndLon() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", "5.0");
        queryParams.put("lon", "5.0");
        requestHandler.validate(null, queryParams);
    }

    @Test
    void handleValidationNeitherLatAndLon() {
        requestHandler.validate(null, new HashMap<>());
    }

    @Test
    void handleValidationOneOfLatAndLon() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", "5.0");

        Exception expected = assertThrows(GoodCoffeeException.class,
                () -> requestHandler.validate(null, queryParams));
    }

    @Test
    void parseEmptyString() throws Exception {
        assertNull(requestHandler.parse(""));
    }

    @Test
    void parseNull() throws Exception {
        assertNull(requestHandler.parse(null));
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }

    private CoffeeShop createCoffeeShop(Point location, String name)
    {
        CoffeeShop shop = new CoffeeShop();
        shop.setLocation(location);
        shop.setName(name);
        return shop;
    }

    private CoffeeShopResponse createShopResponse(Point location, String name) {
        CoffeeShopResponse response = new CoffeeShopResponse();
        response.setLocation(location);
        response.setName(name);
        return response;
    }
}