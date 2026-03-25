package com.wipro.its.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // The name we give our security scheme — referenced below
    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // ── 1. API metadata shown at the top of swagger-ui.html ──
                .info(new Info()
                        .title("Interview Tracking System API")
                        .version("2.0.0")
                        .description("""
                                REST API for the Interview Tracking System.

                                **User Types & Credentials (dev only):**
                                - ADMIN    → ADMIN001 / admin123
                                - Tech Panel → TECH001 / tech123
                                - HR Panel   → HR001 / hr123

                                **How to authenticate:**
                                1. Call POST /api/auth/login with your credentials
                                2. Copy the token from the response
                                3. Click the 'Authorize' button above and paste: Bearer <token>
                                """)
                        .contact(new Contact()
                                .name("ITS Learning Project")
                                .email("admin@wipro.com")))

                // ── 2. Tell Swagger UI about the JWT security scheme ──
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH,
                                new SecurityScheme()
                                        .name(BEARER_AUTH)
                                        .type(SecurityScheme.Type.HTTP)  // HTTP scheme (not apiKey)
                                        .scheme("bearer")                // means "Bearer token"
                                        .bearerFormat("JWT")             // cosmetic hint in the UI
                        ))

                // ── 3. Apply JWT auth globally to all endpoints ──
                // This adds a padlock icon to every endpoint in swagger-ui.html.
                // Without this, you'd have to add @SecurityRequirement on each controller.
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
