package br.com.minhasecretariavirtual.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayForceConfig {

    private final Flyway flyway;

    @PostConstruct
    public void migrate() {
        System.out.println(">>> FORCING FLYWAY MIGRATION <<<");
        flyway.migrate();
    }
}
