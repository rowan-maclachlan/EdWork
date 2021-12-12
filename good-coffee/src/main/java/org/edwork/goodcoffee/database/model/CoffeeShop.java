package org.edwork.goodcoffee.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.edwork.goodcoffee.config.ShopConstants;
import org.geojson.Point;

import javax.validation.constraints.*;

@DynamoDBTable(tableName=ShopConstants.TABLE_NAME)
public class CoffeeShop {

    private static final String GSI_PARTITION = "COFFEE";

    @Size(min= ShopConstants.SHOP_NAME_MIN_SIZE, max=ShopConstants.SHOP_NAME_MAX_SIZE)
    private String name;
    @NotNull
    private Point location;
    @Min(ShopConstants.SHOP_RATING_MIN)
    @Max(ShopConstants.SHOP_RATING_MAX)
    private int rating;
    private String gsiPartition = GSI_PARTITION;

    @DynamoDBHashKey(attributeName="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBTypeConverted(converter = LocationConverter.class)
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "rating", attributeName = "rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "rating", attributeName = "gsiPartition")
    public String getGsiPartition() {
        return gsiPartition;
    }

    public void setGsiPartition(String gsiPartition) {
        this.gsiPartition = gsiPartition;
    }
}
