package com.github.commandercool.cloudsurfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CloudSurferApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudSurferApplication.class, args);
	}

}
