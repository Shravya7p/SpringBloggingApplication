package com.springblogapp.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.models.Contact;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SwaggerConfig {
	public static final String AUTHORIZATION_HEADER="Authorization";
	
	private ApiKey apiKeys() {
		return new ApiKey("JWT",AUTHORIZATION_HEADER,"header");
	}
	
	private List<SecurityContext> securityContexts(){
		return Arrays.asList(SecurityContext.builder().securityReferences(sf()).build());
	}
	
	private List<SecurityReference> sf(){
		AuthorizationScope scopes = new AuthorizationScope("global","accessEverything"); 
		return Arrays.asList(new SecurityReference("JWT",new AuthorizationScope[] {scopes}));
	}
	
	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getInfo())
//				.securityContexts(securityContexts())
				.securityContexts(securityContexts())
				.securitySchemes(Arrays.asList(apiKeys()))
				.select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
	}
	
	private ApiInfo getInfo() {
		
		return new ApiInfo("Blogging Application",
				"This project is completely backend",
				"1.0","Terms Of Service",
				new Contact("lazyCoder",
						"https://lazycoder.com","lazyCoder@gmail.com"),
				"License of APIs","API License URL",Collections.emptyList());
		
	};

}
