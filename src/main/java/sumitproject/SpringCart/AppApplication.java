package sumitproject.SpringCart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		System.out.println("DB_URL = " + System.getenv("DB_URL"));
		System.out.println("JWT_SECRET_KEY = " + System.getenv("JWT_SECRET_KEY"));
		SpringApplication.run(AppApplication.class, args);

	}

}
