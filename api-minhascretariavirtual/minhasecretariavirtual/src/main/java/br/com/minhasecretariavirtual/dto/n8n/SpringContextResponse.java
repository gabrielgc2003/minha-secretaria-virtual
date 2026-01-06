package br.com.minhasecretariavirtual.dto.n8n;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

// O que o Spring vai devolver para o n8n alimentar a IA (Passo 3)
@Data
@Builder
public class SpringContextResponse {
    private UUID userId;
    private String userName;
    private String conversationState; // Ex: INICIAL, COMPRA, SUPORTE
    private List<String> history;     // As últimas 10 mensagens para contexto da IA
    private String prompt;   // Instruções gerais para a IA
    private String instructions;      // Se tiver regras específicas
}
