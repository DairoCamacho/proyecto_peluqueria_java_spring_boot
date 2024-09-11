package com.unaux.dairo.api.infra.swagger;

import org.springframework.http.HttpHeaders;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API RESTful for Hair Salon")
                        .version("0.4.1")
                        .description("API RESTful for Hair Salon")
                        .termsOfService("https://www.unaux.com/terms")
                        .contact(new Contact()
                                .name("Dairo API Support")
                                .email("dalecc08@outlook.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development Server"),
                        new Server().url("https://dairo-api.unaux.com").description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Security Token"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Security Token", new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(In.HEADER)
                                .name(HttpHeaders.AUTHORIZATION)
                                .description("JWT Token")));
    }

  @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("private")
                .pathsToMatch("/api/**")
                .build();
    }

}
