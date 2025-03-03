package org.team1.keyduck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KeyduckApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeyduckApplication.class, args);
    }

}
