package api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Example").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth")); // Requer autenticação
    }

    @Bean
    public SecurityScheme basicAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic") // Define a autenticação como Basic Auth
                .description("Autenticação básica com nome de usuário e senha");
    }
}
