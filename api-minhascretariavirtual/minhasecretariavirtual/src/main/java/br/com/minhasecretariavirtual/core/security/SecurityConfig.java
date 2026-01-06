package br.com.minhasecretariavirtual.core.security;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TenantSecurityFilter tenantSecurityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas (Webhooks, Documentação)
                        .requestMatchers("/api/v1/webhook/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Todo o resto exige que o TenantSecurityFilter tenha autenticado
                        .anyRequest().authenticated()
                )
                // Adiciona nosso filtro ANTES do filtro padrão de usuário/senha
                .addFilterBefore(tenantSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}