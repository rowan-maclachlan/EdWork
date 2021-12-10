package org.edwork.goodcoffee.database;

import dagger.Component;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.modules.StoreModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = StoreModule.class)
public interface ShopStore {

    void addShop(CoffeeShop shop) throws Exception;

    CoffeeShop getShop(String id) throws Exception;
}
