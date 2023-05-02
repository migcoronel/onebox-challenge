package com.example.challenge.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String PROTOCOL_HTTP = "http";

	@Autowired(required = false)
	private BuildProperties buildProperties;
	@Autowired
	private Environment env;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(metadata())
				.protocols(getProtocols())
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build();
	}



	private Set<String> getProtocols() {
		Set<String> protocols = new HashSet<>();
		protocols.add(PROTOCOL_HTTP);
		return protocols;
	}

	@Bean
	public ApiInfo metadata() {
		String contactName = env.getProperty("info.app.contact-name", "ONEBOX CHALLENGE API");
		String contactUrl = env.getProperty("info.app.contact-url", "http://www.abc.com/contact");
		String contactEmail = env.getProperty("info.app.contact-email", "miguel.coronel@xcaleconsulting.com");

		return new ApiInfoBuilder().title(env.getProperty("info.app.name", "ONEBOX CHALLENGE API"))
				.description(env.getProperty("info.app.description", "ONEBOX CHALLENGE API"))
				.version(buildProperties == null ? "" : buildProperties.getVersion())
				.contact(new Contact(contactName, contactUrl, contactEmail)).build();
	}
}
