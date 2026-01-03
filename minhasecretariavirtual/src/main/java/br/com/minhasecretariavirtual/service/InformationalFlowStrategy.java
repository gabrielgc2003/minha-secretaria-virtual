package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.enums.ConversationAction;
import br.com.minhasecretariavirtual.enums.ConversationState;
import br.com.minhasecretariavirtual.enums.UserIntent;
import br.com.minhasecretariavirtual.model.Conversation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InformationalFlowStrategy implements ConversationFlowStrategy {

    private final SimpleIntentParser intentParser;
    private IntentResolverService intentResolver;

    @Override
    public ConversationResponseDTO nextStep(
            Conversation conversation,
            String message
    ) {

        return switch (conversation.getState()) {

            case NOVO_CONTATO -> handleNewContact(conversation);

            case AGUARDANDO_INTENCAO ->
                    handleIntent(conversation, message);

            default -> handleEscalation(conversation);
        };
    }

    private ConversationResponseDTO handleNewContact(
            Conversation conversation
    ) {
        conversation.setState(ConversationState.AGUARDANDO_INTENCAO);
        return askIntent();
    }

    private ConversationResponseDTO handleIntent(
            Conversation conversation,
            String message
    ) {
        UserIntent intent =
                intentResolver.resolve(
                        conversation.getTenantId(),
                        message
                );

        return switch (intent) {

            case INFORMATION -> {
                conversation.setState(ConversationState.FINALIZADO);
                yield inform();
            }

            case HUMAN -> {
                conversation.setState(ConversationState.ESCALADO_HUMANO);
                yield escalate();
            }

            default -> askIntentAgain();
        };
    }

    private ConversationResponseDTO askIntent() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_INTENT)
                .context(
                        Map.of("options",
                                List.of("Informações", "Atendente"))
                )
                .build();
    }

    private ConversationResponseDTO askIntentAgain() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_INTENT_AGAIN)
                .context(
                        Map.of("options",
                                List.of("Informações", "Atendente"),
                                "error", "INVALID_OPTION")
                )
                .build();
    }

    private ConversationResponseDTO inform() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.INFORM)
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO escalate() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ESCALATE_HUMAN)
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO handleEscalation(
            Conversation conversation
    ) {
        conversation.setState(ConversationState.ESCALADO_HUMANO);
        return escalate();
    }
}


