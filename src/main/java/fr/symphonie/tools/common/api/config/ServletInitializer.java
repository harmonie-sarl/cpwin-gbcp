package fr.symphonie.tools.common.api.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import fr.symphonie.tools.common.api.SignatureApiApplication;



public class ServletInitializer extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SignatureApiApplication.class);
	}

}
