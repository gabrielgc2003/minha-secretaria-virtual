package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.model.Tenant;
import br.com.minhasecretariavirtual.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    /**
     * Retorna o tenant atual com base no tenantId
     * extraído do JWT (TenantContext).
     *
     * Se não existir, é erro grave de configuração.
     */
    public Tenant getCurrentTenant() {
        UUID tenantId = TenantContext.getTenant();

        return tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Tenant não encontrado para o id: " + tenantId
                        )
                );
    }
}

