package br.com.minhasecretariavirtual.dto.n8n;

import lombok.Data;

// O que o n8n vai enviar para o Spring (Passo 2)
@Data
public class N8nMessageInput {
    private String remoteJid;   // Telefone (5541999...)
    private String pushName;    // Nome do usuário
    private String messageText; // A mensagem recebida
    private String messageType; // text, image, audio
}
