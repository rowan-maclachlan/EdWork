package org.edwork.goodcoffee.database.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.edwork.goodcoffee.apigateway.ShopConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class CoffeeShop {

    @NotBlank
    private String id;
    @Size(min= ShopConstants.SHOP_NAME_MIN_SIZE, max=ShopConstants.SHOP_NAME_MAX_SIZE)
    private String name;
    @NotBlank
    private String location;
    @Min(ShopConstants.SHOP_RATING_MIN)
    @Max(ShopConstants.SHOP_RATING_MAX)
    private int rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
