package br.com.minhasecretariavirtual.dto.n8n;

import lombok.Data;

// O que o n8n envia de volta após a IA processar (Passo 6)
@Data
public class N8nAgentOutput {
    private String remoteJid;
    private String iaResponse;    // A resposta que a IA gerou
    private String detectedIntent; // A intenção que a IA classificou
    private String newState;       // Se a IA decidiu mudar o estado
}
