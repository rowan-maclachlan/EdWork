package org.edwork.goodcoffee.apigateway.dto;

import org.edwork.goodcoffee.config.ShopConstants;
import org.geojson.Point;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class DeleteShopResponse {
    @Size(min= ShopConstants.SHOP_NAME_MIN_SIZE, max=ShopConstants.SHOP_NAME_MAX_SIZE)
    private String name;
    @Size(min=2, max=2)
    private Point location;
    @Min(ShopConstants.SHOP_RATING_MIN)
    @Max(ShopConstants.SHOP_RATING_MAX)
    private int rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
