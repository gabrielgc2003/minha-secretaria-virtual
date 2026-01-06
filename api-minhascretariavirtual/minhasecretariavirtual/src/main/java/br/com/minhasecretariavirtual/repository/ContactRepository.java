package br.com.minhasecretariavirtual.repository;

import br.com.minhasecretariavirtual.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
    // Busca um contato específico dentro de um tenant (segurança multi-tenant)
    Optional<Contact> findByRemoteJidAndTenantId(String remoteJid, UUID tenantId);
}
