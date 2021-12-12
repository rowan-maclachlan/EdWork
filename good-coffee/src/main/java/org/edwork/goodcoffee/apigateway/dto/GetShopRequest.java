package org.edwork.goodcoffee.apigateway.dto;

import jakarta.validation.constraints.Size;
import org.edwork.goodcoffee.config.ShopConstants;

import javax.validation.constraints.NotBlank;

public class GetShopRequest {
    @NotBlank
    @Size(min=ShopConstants.SHOP_NAME_MIN_SIZE, max=ShopConstants.SHOP_NAME_MAX_SIZE)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
