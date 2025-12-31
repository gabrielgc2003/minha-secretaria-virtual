package br.com.minhasecretariavirtual.repository;

import br.com.minhasecretariavirtual.model.ConversationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationDataRepository extends JpaRepository<ConversationData, UUID> {

    Optional<ConversationData> findByConversationId(UUID conversationId);
}

