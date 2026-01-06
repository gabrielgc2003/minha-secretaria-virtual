package br.com.minhasecretariavirtual.model;

import br.com.minhasecretariavirtual.model.enums.MessageRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_message_contact", columnList = "contact_id"), // Busca rápida por dono da mensagem
        @Index(name = "idx_message_tenant", columnList = "tenant_id"),   // Segurança e performance
        @Index(name = "idx_message_created_at", columnList = "createdAt") // Para ordenar histórico rapidamente
})
@Getter
@Setter
@SuperBuilder // CRUCIAL: Permite preencher campos do BaseEntity (id, tenantId, createdAt)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Inclui o ID da BaseEntity na comparação
public class Message extends BaseEntity {

    @Column(columnDefinition = "TEXT", nullable = false) // Garante que o Postgres use TEXT (sem limite de 255 chars)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageRole role; // USER, ASSISTANT ou SYSTEM

    @Column(length = 20)
    @Builder.Default // Garante valor padrão se não informado
    private String type = "text"; // text, image, audio, document

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}