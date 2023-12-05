package com.rmf.demoparkapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringDocOpenApiConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Demo Park API")
                                .description("This is a project Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
                                .version("v1")
                                .license(new License().name("Apache 2.8").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }
}
