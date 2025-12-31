package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.enums.ConversationState;
import br.com.minhasecretariavirtual.model.Conversation;
import br.com.minhasecretariavirtual.model.ServiceCatalogItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchedulingFlowStrategy implements ConversationFlowStrategy {

    private final FeatureService featureService;
    private final ServiceCatalogService catalogService;

    @Override
    public ConversationResponseDTO nextStep(Conversation conversation) {

        switch (conversation.getState()) {

            case NOVO_CONTATO:
                conversation.setState(ConversationState.AGUARDANDO_SERVICO);
                return askService();

            case AGUARDANDO_SERVICO:
                conversation.setState(ConversationState.AGUARDANDO_DATA);
                return askDate();

            case AGUARDANDO_DATA:
                conversation.setState(ConversationState.AGUARDANDO_CONFIRMACAO);
                return confirm();

            case AGUARDANDO_CONFIRMACAO:
                conversation.setState(ConversationState.FINALIZADO);
                return finish();

            default:
                conversation.setState(ConversationState.ESCALADO_HUMANO);
                return escalate();
        }
    }

    private ConversationResponseDTO askService() {
        return ConversationResponseDTO.builder()
                .action("ASK_SERVICE")
                .context(
                        Map.of(
                                "services",
                                catalogService.getActiveServices()
                                        .stream()
                                        .map(ServiceCatalogItem::getName)
                                        .toList()
                        )
                )
                .build();
    }

    private ConversationResponseDTO askDate() {

        if (!featureService.isAgendaEnabled()) {
            return ConversationResponseDTO.builder()
                    .action("FORWARD_TO_PROFESSIONAL")
                    .context(Map.of())
                    .build();
        }

        return ConversationResponseDTO.builder()
                .action("ASK_DATE")
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO confirm() {
        return ConversationResponseDTO.builder()
                .action("ASK_CONFIRMATION")
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO finish() {
        return ConversationResponseDTO.builder()
                .action("FINISH")
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
