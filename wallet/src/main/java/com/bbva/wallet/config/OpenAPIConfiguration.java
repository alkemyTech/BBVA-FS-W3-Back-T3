package com.bbva.wallet.config;

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
        info=@Info(
                title = "MiaWallet",
                version = "1.0.0",
                description = "MiaWallet, la mejor billetera para tu dinero",
                contact=@Contact(
                        name = "VikatCode",
                        email = "VikatCode@bbva.com"
                ),
                license = @License(
                        name = "MiaWallet license",
                        url = "miawallet.bbva.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local dev ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod dev ENV",
                        url = "miawallet.bbva.com"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenAPIConfiguration {
}