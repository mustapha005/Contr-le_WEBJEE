package ma.enset.mustaphaaarab.examjee.config;

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
    @Bean
    public OpenAPI vehicleRentalOpenApi() {
        String schemeName = "bearerAuth";
        SecurityScheme bearerScheme = new SecurityScheme()
                .name(schemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Rental API - Exam JEE")
                        .version("1.0.0")
                        .description("API REST securisee par JWT pour gerer agences, vehicules et locations")
                        .contact(new Contact().name("VotreNom VotrePrenom")))
                .components(new Components().addSecuritySchemes(schemeName, bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList(schemeName));
    }
}
