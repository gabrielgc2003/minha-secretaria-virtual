package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.model.ServiceCatalogItem;
import br.com.minhasecretariavirtual.repository.ServiceCatalogItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceCatalogService {

    private final ServiceCatalogItemRepository repository;
    private final TenantService tenantService;

    /**
     * Retorna a lista de serviços ativos
     * do tenant atual.
     *
     * Isso alimenta a IA com opções reais.
     */
    public List<ServiceCatalogItem> getActiveServices() {
        UUID tenantId = tenantService.getCurrentTenant().getId();
        return repository.findByTenantIdAndActiveTrue(tenantId);
    }
}
