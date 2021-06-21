package com.gunyoung.tmb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TouchMyBodyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouchMyBodyApplication.class, args);
	}

}
