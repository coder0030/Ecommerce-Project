package sumitproject.SpringCart.AppConfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig {

     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

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

}
