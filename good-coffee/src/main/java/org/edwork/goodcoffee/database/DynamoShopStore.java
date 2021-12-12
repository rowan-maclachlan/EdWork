package org.edwork.goodcoffee.database;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import org.edwork.goodcoffee.database.model.CoffeeShop;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DynamoShopStore implements ShopStore {

    private static final String RANGE_KEY_ATTRIBUTE_NAME = "rating";
    private static final String GSI_PARTITION_KEY_NAME = "gsiPartition";

    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public DynamoShopStore(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public void addShop(CoffeeShop shop) {
        dynamoDBMapper.save(shop);
    }

    @Override
    public CoffeeShop getShop(String name) {
        return dynamoDBMapper.load(CoffeeShop.class, name);
    }

    @Override
    public Stream<CoffeeShop> getShops(int rating) {
        CoffeeShop gsiHashKeyObj = new CoffeeShop();
        DynamoDBQueryExpression<CoffeeShop> exp = new DynamoDBQueryExpression<>();
        exp.withIndexName(GSI_PARTITION_KEY_NAME)
                .withHashKeyValues(gsiHashKeyObj)
                .withRangeKeyCondition(RANGE_KEY_ATTRIBUTE_NAME,  new Condition().withComparisonOperator(ComparisonOperator.GE));
        return dynamoDBMapper.query(CoffeeShop.class, exp).stream();
    }
}
