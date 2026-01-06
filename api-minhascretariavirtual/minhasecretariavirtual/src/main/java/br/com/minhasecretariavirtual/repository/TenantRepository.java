package br.com.minhasecretariavirtual.repository;

import br.com.minhasecretariavirtual.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByApiKeyEvolution(String apiKey);
    Optional<Tenant> findByInstanceName(String instanceName);
}
