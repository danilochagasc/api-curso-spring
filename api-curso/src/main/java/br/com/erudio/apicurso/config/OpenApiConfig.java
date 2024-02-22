package br.com.erudio.apicurso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RESTful API with Java 18 and Spring Boot 3") //titulo da documentacao
                        .version("v1") //versao da documentacao
                        .description("Some description about your API") //descricao da documentacao
                        .termsOfService("www.google.com.br") //link para os termos de servico
                        .license(
                                new License()
                                        .name("Apache 2.0") //nome da licenca
                                        .url("www.google.com.br") //link para a licenca
                        )
                );
    }

}
