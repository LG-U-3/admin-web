package com.example.adminweb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("관리자 화면 API")
            .description("유레카 종합프로젝트 3조 관리자화면 API입니다.")
            .version("1.0.0"));
  }
}