package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final TenantService tenantService;

    @Value("${FEATURE_AGENDA_ENABLED:true}")
    private boolean agendaGloballyEnabled;

    /**
     * Verifica se a funcionalidade de agenda
     * está habilitada para o tenant atual.
     *
     * Regra:
     * - precisa estar ativa globalmente
     * - precisa estar ativa no tenant
     */
    public boolean isAgendaEnabled() {
        if (!agendaGloballyEnabled) {
            return false;
        }

        Tenant tenant = tenantService.getCurrentTenant();
        return tenant.getFeatures().isAgendaEnabled();
    }
}
