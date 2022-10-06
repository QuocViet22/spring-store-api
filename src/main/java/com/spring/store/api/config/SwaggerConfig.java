package com.spring.store.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.Security;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//    public static final String AUTHORIZATION_HEADER = "Authorization";
//
//    private ApiKey apiKeys() {
//        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
//    }
//
//    private List<SecurityContext> securityContexts() {
//        return Arrays.asList(SecurityContext.builder().securityReferences(sf).build());
//    }
//
//    private List<SecurityReference> sf() {
//        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
//        return Arrays.asList(new SecurityReference("scope", new AuthorizationScope[]{scope}));
//    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
//                .securityContexts(securityContexts())
//                .securitySchemes(Arrays.asList(apiKeys()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact("shoes-store", "", "tlcn092022@gmail.com");
        return new ApiInfoBuilder()
                .title("Shoes Store API")
                .description("Documentation Shoes Store API")
                .version("1.0.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }
}
