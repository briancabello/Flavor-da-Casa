package nbcc.resto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"nbcc.resto", "nbcc.auth", "nbcc.common", "nbcc.email"})
//@EnableJpaRepositories({"nbcc.resto.repository"})
//@EntityScan({"nbcc.resto.entity"})
public class RestaurantApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApiApplication.class, args);
    }
}
