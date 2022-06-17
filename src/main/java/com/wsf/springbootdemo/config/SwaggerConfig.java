package com.wsf.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author wsf
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket desertsApi(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wsf.springbootdemo.controller"))
                .paths(PathSelectors.any())
                .build()
                .groupName("default")
                .enable(true);
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("API说明文档")
                .description("人事管理系统(权限控制系统) API说明文档")
                .contact(new Contact("wsf, "https://github.com/samsarawsf/springbootDemo", "1529907320@qq.com"))
                .termsOfServiceUrl("https://github.com/samsarawsf/springbootDemo")
                .version("1.0")
                .build();
    }
}
