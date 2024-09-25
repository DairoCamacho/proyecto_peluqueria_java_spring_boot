package com.unaux.dairo.api.infra.swagger;

import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
                            title = "API RESTful for Hair Salon", 
                            description = "API RESTful for Hair Salon", 
                            version = "0.4.1", 
                            termsOfService = "https://www.unaux.com/terms", 
                            contact = @Contact(name = "Dairo API Support", 
                              email = "dalecc08@outlook.com"),
                            license = @License(
                              name = "Apache 2.0", 
                              url = "https://www.apache.org/licenses/LICENSE-2.0")  
                              ),
                          servers ={
                            @Server(url = "http://localhost:8080", 
                                    description = "Development Server"),
                            @Server(url = "https://dairo-api.unaux.com", 
                                    description = "Production Server")
                          },
                          security = @SecurityRequirement(name = "Security Token")
                  )
@SecurityScheme(type = SecuritySchemeType.HTTP, 
                name = "Security Token", 
                description = "JWT Token",
                paramName = HttpHeaders.AUTHORIZATION,
                in = SecuritySchemeIn.HEADER,
                scheme = "bearer", 
                bearerFormat = "JWT")
public class SwaggerConfig {

}
