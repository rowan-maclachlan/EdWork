package org.edwork.goodcoffee.database;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.services.MapperService;

import javax.inject.Inject;

public class DynamoShopStore implements ShopStore {

    private static final String TABLE_NAME = "tableName";

    @Inject
    private final DynamoDB dynamoDB;
    @Inject
    private final MapperService mapperService;

    public DynamoShopStore(DynamoDB dynamoDB, MapperService mapperService) {
        this.dynamoDB = dynamoDB;
        this.mapperService = mapperService;
    }

    @Override
    public void addShop(CoffeeShop shop) throws Exception {
        System.out.println("Creating new shop: " + mapperService.toString(shop));
        Item newShopItem = new Item()
                .withPrimaryKey("id", shop.getId())
                .withString("name", shop.getName())
                .withString("location", shop.getLocation())
                .withNumber("rating", shop.getRating());

        PutItemOutcome outcome = dynamoDB.getTable(TABLE_NAME).putItem(newShopItem);
        System.out.println(mapperService.toString(outcome));
    }

    @Override
    public CoffeeShop getShop(String id) throws Exception {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", id);

        System.out.println("Getting shop " + id);
        Item outcome = dynamoDB.getTable(TABLE_NAME).getItem(spec);
        System.out.println("GetItem succeeded: " + outcome);

        return mapperService.fromString(CoffeeShop.class, outcome.toJSON());
    }
}
