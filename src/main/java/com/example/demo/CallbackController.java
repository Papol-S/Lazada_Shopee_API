package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class CallbackController {
    @GetMapping("/test1")
    public String getTest (){
        return "Hello";
    }
    @PostMapping ("/test2")
    public String postTest (@RequestBody SellerIdResponse sellerIdResponse){
        System.out.println(sellerIdResponse);
        return "hi";
    }


}
