package org.edwork.goodcoffee.database;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import org.edwork.goodcoffee.database.model.CoffeeShop;
import org.edwork.goodcoffee.services.LoggingService;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DynamoShopStore implements ShopStore {

    private static final String RANGE_KEY_ATTRIBUTE_NAME = "rating";
    private static final String GSI_INDEX_NAME = "rating";

    private final DynamoDBMapper dynamoDBMapper;
    private final LoggingService loggingService;

    @Inject
    public DynamoShopStore(DynamoDBMapper dynamoDBMapper, LoggingService loggingService) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.loggingService = loggingService;
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
        DynamoDBQueryExpression<CoffeeShop> exp = new DynamoDBQueryExpression<>();
        Condition rangeKeyCondition = new Condition().withComparisonOperator(ComparisonOperator.GE)
                .withAttributeValueList(new AttributeValue().withN(Integer.toString(rating)));
        exp.withIndexName(GSI_INDEX_NAME)
                .withHashKeyValues(new CoffeeShop())
                .withRangeKeyCondition(RANGE_KEY_ATTRIBUTE_NAME, rangeKeyCondition)
                .withConsistentRead(false);
        loggingService.info(exp.toString());
        return dynamoDBMapper.query(CoffeeShop.class, exp).stream();
    }

    @Override
    public void deleteShop(CoffeeShop shop) {
        dynamoDBMapper.delete(shop);
    }
}
