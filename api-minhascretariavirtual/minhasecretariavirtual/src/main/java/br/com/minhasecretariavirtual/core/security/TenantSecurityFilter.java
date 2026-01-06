package br.com.minhasecretariavirtual.core.security;

import br.com.minhasecretariavirtual.core.config.TenantContext; // Se você tiver essa classe de Contexto
import br.com.minhasecretariavirtual.model.Tenant;
import br.com.minhasecretariavirtual.repository.TenantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class TenantSecurityFilter extends OncePerRequestFilter {

    private final TenantRepository tenantRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Ler o Token do Header (Padrão: X-Api-Key ou X-Tenant-Token)
        String apiKey = request.getHeader("X-Tenant-Token");

        // 2. Se não enviou token, segue o fluxo (o SecurityConfig vai barrar se a rota for privada)
        if (apiKey == null || apiKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Buscar qual Tenant é dono dessa chave
        var tenantOptional = tenantRepository.findByApiKeyEvolution(apiKey);

        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();

            // 4. (Opcional) Validar se o Tenant está ativo
            if (!tenant.isActive()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant inativo");
                return;
            }

            // 5. Autenticar no Spring Security
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    tenant, // Principal (O objeto Tenant logado)
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_TENANT"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            TenantContext.setTenant(tenant.getId());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}