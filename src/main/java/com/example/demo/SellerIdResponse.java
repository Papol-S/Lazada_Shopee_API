package com.example.demo;

import lombok.Data;

@Data
public class SellerIdResponse {
    private String seller_id;

    @Override
    public String toString() {
        return "Seller ID = " +
                seller_id;
    }
}
