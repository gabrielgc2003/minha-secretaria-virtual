package br.com.minhasecretariavirtual.repository;

import br.com.minhasecretariavirtual.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByTenantIdAndPhone(
            UUID tenantId,
            String phone
    );
}
