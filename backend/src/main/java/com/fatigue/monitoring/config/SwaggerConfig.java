package com.fatigue.monitoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * Swagger配置
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${swagger.enabled:false}")
    private Boolean enabled;

    @Value("${swagger.title:}")
    private String title;

    @Value("${swagger.description:}")
    private String description;

    @Value("${swagger.version:}")
    private String version;

    @Value("${swagger.base-package:}")
    private String basePackage;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS__30)
                .enable(enabled)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(new Contact("Fatigue Monitoring System", "", ""))
                .version(version)
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(operationContext -> true)
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(new SecurityReference("Authorization", new AuthorizationScope[]{authorizationScope}));
    }
}
