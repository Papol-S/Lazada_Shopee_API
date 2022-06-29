package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;

//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import java.nio.charset.StandardCharsets;
//import java.sql.SQLOutput;
//import java.time.ZonedDateTime;
//import java.util.Collections;
@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
//	public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

//		WebTestClient testClient = (WebTestClient) WebTestClient
//				.bindToServer()
//				.baseUrl("http://worldtimeapi.org/api/timezone/Asia")
//				.build()
//				.get()
//				.uri("/Bangkok")
//				.exchange()
//				.expectStatus().isCreated()
//				.expectHeader().valueEquals("Content-Type", "application/json")
//				.expectBody().jsonPath("field").isEqualTo("value");

//		Mono<String> response = WebClient.create()
//				.get()
//				.uri("http://worldtimeapi.org/api/timezone/Asia/Bangkok")
//				.retrieve()
//				.bodyToMono(String.class);
//		response.subscribe(log::info);
//		response.subscribe(res -> System.out.println(res));



//		log.info("Start");
//		log.info(String.valueOf(testClient));
//		log.info("end");
//		System.exit(0);



	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
				= "http://worldtimeapi.org/api/timezone/Asia/Bangkok";
		ResponseEntity<WorldTimeResponse> response
				= restTemplate.getForEntity(fooResourceUrl, WorldTimeResponse.class);
		log.info(String.valueOf(response.getBody()));

		WorldTimeResponse test = new WorldTimeResponse();
		System.out.println("start");
		System.out.println(test.toString());
//		System.exit(0);

//		System.out.println(response.getBody());
	}
}