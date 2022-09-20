package fr.symphonie.tools.common.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"fr.symphonie.pesv2.tools.common.services","fr.symphonie.tools.common.api.api.controller"})
@EntityScan(basePackages = {"fr.symphonie.tools.common.api.model"})
public class SignatureApiApplication  {
	
	public static void main(String[] args) {
		SpringApplication.run(SignatureApiApplication.class, args);

	}

}
