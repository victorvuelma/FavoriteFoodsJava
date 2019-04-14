package me.vuelma.favoritefoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FavoriteFoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavoriteFoodsApplication.class, args);
    }

}