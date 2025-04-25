package org.hopto.fjavierjp.petregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PetregistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetregistryApplication.class, args);
	}
}
