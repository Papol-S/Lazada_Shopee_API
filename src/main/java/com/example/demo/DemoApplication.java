package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//		WebClient client = WebClient.builder()
//				.baseUrl("http://localhost:8080")
//				.defaultCookie("cookieKey", "cookieValue")
//				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//				.defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
//				.build();
	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
				= "http://www.majorcineplex.com/cinema";
		ResponseEntity<String> response
				= restTemplate.getForEntity(fooResourceUrl, String.class);
		log.info(response.getBody());

//		System.out.println(response.getBody());
	}
}