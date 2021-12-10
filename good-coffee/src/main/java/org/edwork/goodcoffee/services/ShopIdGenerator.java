package org.edwork.goodcoffee.services;

public class ShopIdGenerator implements IdGenerator {

    public ShopIdGenerator() {}

    @Override
    public String generateId() {
        return java.util.UUID.randomUUID().toString();
    }
}
