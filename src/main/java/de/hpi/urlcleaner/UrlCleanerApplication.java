package de.hpi.urlcleaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class UrlCleanerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlCleanerApplication.class, args);
	}
}
