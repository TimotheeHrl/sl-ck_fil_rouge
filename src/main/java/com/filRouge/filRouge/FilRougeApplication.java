package com.filRouge.filRouge;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@RequiredArgsConstructor
@EnableSwagger2
public class FilRougeApplication {
	public static void main(String[] args) {
		SpringApplication.run(FilRougeApplication.class, args);
	}
}
