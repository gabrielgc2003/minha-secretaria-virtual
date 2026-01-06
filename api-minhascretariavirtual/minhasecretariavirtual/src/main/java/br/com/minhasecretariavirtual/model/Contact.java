package br.com.minhasecretariavirtual.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts", indexes = {
        // ATENÇÃO: columnList usa o nome da coluna no Banco (snake_case), não o nome da variável Java
        @Index(name = "idx_contact_remote_jid", columnList = "remote_jid"),
        @Index(name = "idx_contact_tenant", columnList = "tenant_id")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Garante comparação correta usando o ID do pai
public class Contact extends BaseEntity {

    @Column(name = "remote_jid", nullable = false)
    private String remoteJid;

    @Column(name = "push_name")
    private String pushName;

    @Column(name = "conversation_state", length = 50)
    private String conversationState;

    @Builder.Default
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
}