package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;
@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
//	public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
				= "https://api.lazada.co.th/rest/order/get?order_id=560152067664242&app_key=110176&sign_method=sha256&access_token=50000701a21kRuupeXggv8PzTehmQgXhy1cb62addvDexGOODPtvuQpzxLwkN1Sw&timestamp=1657449811786&sign=BF0193AFF923E84A13A73493397E66401B6BD7ED3CDBC9DBDABEA94D1156F4A7";
		ResponseEntity<OrderIdResponse> response
				= restTemplate.getForEntity(fooResourceUrl, OrderIdResponse.class);
		log.info(String.valueOf(response.getBody()));

	}
}