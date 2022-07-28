package Client.Lazada.Lazop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("lazadaApi")
@Slf4j
public class CallbackController {
//    @GetMapping("/test1")
//    public String getTest (){
//        return "Hello";
//    }
    @PostMapping //("orderresponse")
    public String postTest (@RequestBody SellerIdResponse sellerIdResponse){
        log.info(String.valueOf(sellerIdResponse));
        return "hi";

    }
}
