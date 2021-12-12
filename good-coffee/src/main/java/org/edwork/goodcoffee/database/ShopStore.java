package org.edwork.goodcoffee.database;

import org.edwork.goodcoffee.database.model.CoffeeShop;

import java.util.stream.Stream;

public interface ShopStore {

    void addShop(CoffeeShop shop);

    CoffeeShop getShop(String id);

    Stream<CoffeeShop> getShops(int rating);
}
