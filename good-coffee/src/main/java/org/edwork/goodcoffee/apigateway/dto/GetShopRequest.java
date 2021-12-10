package org.edwork.goodcoffee.apigateway.dto;

import javax.validation.constraints.NotBlank;

public class GetShopRequest {
    @NotBlank
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
