package com.banking.springboot_bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank Application using SpringBoot",
				description = "Backend REST APIs for Spring Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Ck",
						email = "chandra.kanthr9898@gmail.com",
						url = "https://github.com/chandrakanthrck/BankingApplication"
				),
				license = @License(
						name = "The Bank App License",
						url = "https://github.com/chandrakanthrck/BankingApplication"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Banking Application Documentation",
				url = "https://github.com/chandrakanthrck/BankingApplication"
		)
)
public class SpringbootBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBankApplication.class, args);
	}

}
