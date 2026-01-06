package br.com.minhasecretariavirtual.repository;

import br.com.minhasecretariavirtual.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Busca as últimas X mensagens ordenadas por data (para contexto da IA)
    @Query("SELECT m FROM Message m WHERE m.contact.id = :contactId ORDER BY m.createdAt DESC")
    List<Message> findLatestByContact(@Param("contactId") UUID contactId, Pageable pageable);
}