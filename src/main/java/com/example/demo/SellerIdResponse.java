package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;
@Data
public class SellerIdResponse {
        private String seller_id;
        private String message_type;
        private String order_status;
        private  String trade_order_id;
        private String timestamp;

        @SuppressWarnings("unchecked")
        @JsonProperty("data")
        private void unpackNested(Map<String,Object> data) {
                this.order_status = (String)data.get("order_status");
                this.trade_order_id = (String) data.get("trade_order_id");
        }
}
