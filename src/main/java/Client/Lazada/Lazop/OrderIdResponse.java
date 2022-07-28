package Client.Lazada.Lazop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;
@Data
public class OrderIdResponse {
    private  String price;
    private String item_count;
    private String tax_code;
//    private Character  shipping_fee;
    @JsonProperty("data")
    private void unpackNested(Map<String,Object> data) {
        this.price = (String)data.get("price");
        this.item_count = (String) data.get("item_count");
//        this.shipping_fee = (Character) data.get("shipping_fee");
        this.tax_code = (String) data.get("tax_code");
    }
}
