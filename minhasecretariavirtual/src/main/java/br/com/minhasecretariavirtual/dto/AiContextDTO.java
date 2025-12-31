package br.com.minhasecretariavirtual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class AiContextDTO {

    /**
     * Ação atual do fluxo.
     */
    @NotBlank
    private String action;

    /**
     * Resumo seguro da conversa.
     */
    @NotNull
    private Map<String, Object> context;
}
