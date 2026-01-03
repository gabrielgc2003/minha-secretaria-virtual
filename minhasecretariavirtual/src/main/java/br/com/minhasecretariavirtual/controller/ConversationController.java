package br.com.minhasecretariavirtual.controller;

import br.com.minhasecretariavirtual.dto.ConversationRequestDTO;
import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.model.Conversation;
import br.com.minhasecretariavirtual.service.ConversationFlowService;
import br.com.minhasecretariavirtual.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final ConversationFlowService conversationFlowService;

    /**
     * Endpoint principal de entrada de mensagens
     * vindas do WhatsApp (via n8n).
     */
    @PostMapping("/next-step")
    public ConversationResponseDTO nextStep(
            @Valid @RequestBody ConversationRequestDTO request
    ) {

        // 1️⃣ Busca ou cria a conversa pelo telefone
        Conversation conversation =
                conversationService.getOrCreateConversation(request.getFrom());

        // 2️⃣ Decide o próximo passo do fluxo
        ConversationResponseDTO response =
                conversationFlowService.nextStep(conversation, request.getMessage());

        // 3️⃣ Persiste o novo estado da conversa
        conversationService.save(conversation);

        // 4️⃣ Retorna a ação + contexto
        return response;
    }
}
