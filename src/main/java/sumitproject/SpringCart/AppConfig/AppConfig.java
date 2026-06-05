package sumitproject.SpringCart.AppConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig {

     @Bean
     public ModelMapper modelMapper() {
         return new ModelMapper();
     }

     public String generateTransactionId(Long userId, Long orderId, String paymentMethod) {

         String timePart = LocalDateTime.now()
                 .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

         return paymentMethod.toUpperCase() + "-" +
                 userId + "-" +
                 orderId + "-" +
                 timePart;
     }

     @Bean
     public OpenAPI customOpenAPI() {
         return new OpenAPI()
                 .info(new Info()
                         .title("SpringCart API")
                         .version("1.0.0"))
                 .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
                 .components(new Components()
                         .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                 .type(SecurityScheme.Type.HTTP)
                                 .scheme("bearer")
                                 .bearerFormat("JWT")));
     }
}
