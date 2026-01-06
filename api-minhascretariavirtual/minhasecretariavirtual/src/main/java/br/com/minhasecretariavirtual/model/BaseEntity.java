package br.com.minhasecretariavirtual.model;
import br.com.minhasecretariavirtual.core.config.TenantContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder // Permite que classes filhas usem o Builder incluindo campos desta classe
@NoArgsConstructor // Necessário para o JPA
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = UUID.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Melhor performance e compatibilidade com Postgres
    private UUID id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        // Garante que o TenantID seja injetado automaticamente antes de salvar
        if (this.tenantId == null) {
            this.tenantId = TenantContext.getTenant();
        }

        if (this.tenantId == null) {
            throw new IllegalStateException("Tenant context is missing. Não foi possível identificar o Tenant.");
        }


    }
}