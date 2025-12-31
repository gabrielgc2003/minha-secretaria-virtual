package br.com.minhasecretariavirtual.config;

import br.com.minhasecretariavirtual.enums.Role;
import br.com.minhasecretariavirtual.service.JwtService;
import br.com.minhasecretariavirtual.service.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (request.getRequestURI().startsWith("/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ") ) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.parseToken(token);

            // 1️⃣ Extrai tenant
            UUID tenantId = UUID.fromString(
                    claims.get("tenant_id", String.class)
            );

            // 2️⃣ Extrai role
            String role = claims.get("role", String.class);

            if (!Role.SYSTEM.name().equals(role)) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }

            // 3️⃣ Seta TenantContext
            TenantContext.setTenant(tenantId);

            // 4️⃣ Cria Authentication para o Spring
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            "system",
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_SYSTEM"))
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } finally {
            TenantContext.clear();
        }
    }
}
