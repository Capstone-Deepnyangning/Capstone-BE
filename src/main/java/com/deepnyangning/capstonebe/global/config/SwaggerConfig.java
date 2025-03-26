package com.deepnyangning.capstonebe.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("본교 내 학술정보원 안면인식 프로젝트 API")
                .description("25-1 Capstone 딥냥닝팀")
                .version("1.0.0");
    }
}
