package br.com.minhasecretariavirtual.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "conversation_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationData {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    // Dados coletados
    @Column(name = "selected_service")
    private String selectedService;

    @Column(name = "desired_date")
    private LocalDate desiredDate;

    @Column(name = "desired_period")
    private String desiredPeriod;

    // Campo genérico para evolução futura
    @Column(columnDefinition = "jsonb")
    private String extraData;
}

