package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.enums.ConversationState;
import br.com.minhasecretariavirtual.model.Conversation;
import br.com.minhasecretariavirtual.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    /**
     * Busca uma conversa ativa pelo telefone.
     * Se não existir, cria uma nova.
     */
    public Conversation getOrCreateConversation(String phone) {
        UUID tenantId = TenantContext.getTenant();

        return conversationRepository
                .findTopByTenantIdAndPhoneOrderByUpdatedAtDesc(tenantId, phone)
                .filter(conv -> conv.getState() != ConversationState.FINALIZADO)
                .orElseGet(() -> createNewConversation(tenantId, phone));
    }

    /**
     * Cria uma nova conversa no estado inicial.
     */
    private Conversation createNewConversation(UUID tenantId, String phone) {
        Conversation conversation = new Conversation();
        conversation.setTenantId(tenantId);
        conversation.setPhone(phone);
        conversation.setState(ConversationState.NOVO_CONTATO);
        return conversationRepository.save(conversation);
    }

    /**
     * Persiste alterações de estado da conversa.
     */
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
