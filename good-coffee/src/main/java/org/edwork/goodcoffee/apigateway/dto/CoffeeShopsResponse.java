package org.edwork.goodcoffee.apigateway.dto;

import java.util.List;

public class CoffeeShopsResponse {
    private List<CoffeeShopResponse> coffeeShops;

    public List<CoffeeShopResponse> getCoffeeShops() {
        return coffeeShops;
    }

    public void setCoffeeShops(List<CoffeeShopResponse> coffeeShops) {
        this.coffeeShops = coffeeShops;
    }
}
