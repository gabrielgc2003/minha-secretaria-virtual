package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.enums.ConversationState;
import br.com.minhasecretariavirtual.model.Conversation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InformationalFlowStrategy implements ConversationFlowStrategy {

    @Override
    public ConversationResponseDTO nextStep(Conversation conversation) {

        switch (conversation.getState()) {

            case NOVO_CONTATO:
                conversation.setState(ConversationState.AGUARDANDO_INTENCAO);
                return askIntent();

            case AGUARDANDO_INTENCAO:
                conversation.setState(ConversationState.FINALIZADO);
                return inform();

            default:
                conversation.setState(ConversationState.ESCALADO_HUMANO);
                return escalate();
        }
    }

    private ConversationResponseDTO askIntent() {
        return ConversationResponseDTO.builder()
                .action("ASK_INTENT")
                .context(
                        Map.of("options", List.of("Informações", "Atendente"))
                )
                .build();
    }

    private ConversationResponseDTO inform() {
        return ConversationResponseDTO.builder()
                .action("INFORM")
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO escalate() {
        return ConversationResponseDTO.builder()
                .action("ESCALATE_HUMAN")
                .context(Map.of())
                .build();
    }
}

