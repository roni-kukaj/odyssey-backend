package com.odyssey.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(contact =
        @Contact(name = "Team", email = "email@email.com"),
                description = "OpenAPI Documentation for our application",
                title = "OpenAPI Documentation",
                version = "1.0.0",
                license =
                @License(name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8000")
        }, security = {@SecurityRequirement(name = "BearerAuth")})
@SecurityScheme(name = "BearerAuth", description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class OpenAPIConfig2 {
}