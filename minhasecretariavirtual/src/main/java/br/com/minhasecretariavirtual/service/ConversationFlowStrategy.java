package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.model.Conversation;

public interface ConversationFlowStrategy {
    /**
     * Decide o próximo passo da conversa
     * com base no estado atual.
     */
    ConversationResponseDTO nextStep(Conversation conversation, String message);
}
