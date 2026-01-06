package br.com.minhasecretariavirtual.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String instanceName; // Nome da instância na Evolution API (identificador externo)

    @Column(name = "api_key_evolution")
    private String apiKeyEvolution;

    @Column(columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(columnDefinition = "TEXT")
    private String instruction; // Instrução personalizada para o modelo de linguagem

    private boolean active;
}