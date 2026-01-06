package br.com.minhasecretariavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MinhasecretariavirtualApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinhasecretariavirtualApplication.class, args);
    }

}
