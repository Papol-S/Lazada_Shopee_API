package com.example.demo;

import lombok.Data;
@Data
public class WorldTimeResponse {
    private String datetime;
    private String timezone;
    @Override
    public String toString() {
        return "WorldTimeResponse{" +
                "datetime='" + datetime + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
