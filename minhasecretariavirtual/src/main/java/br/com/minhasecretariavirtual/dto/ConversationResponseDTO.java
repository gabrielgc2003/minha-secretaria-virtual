package br.com.minhasecretariavirtual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class ConversationResponseDTO {

    /**
     * Ação que o n8n/IA deve executar.
     * Ex: ASK_SERVICE, ASK_DATE, FINISH
     */
    @NotBlank
    private String action;

    /**
     * Contexto seguro para a IA gerar a mensagem.
     * Nunca contém dados sensíveis.
     */
    @NotNull
    private Map<String, Object> context;
}
