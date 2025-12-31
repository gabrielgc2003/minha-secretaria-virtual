package br.com.minhasecretariavirtual.repository;

import br.com.minhasecretariavirtual.model.ServiceCatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceCatalogItemRepository extends JpaRepository<ServiceCatalogItem, UUID> {

    List<ServiceCatalogItem> findByTenantIdAndActiveTrue(UUID tenantId);
}
