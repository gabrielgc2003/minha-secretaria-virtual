package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.enums.ConversationAction;
import br.com.minhasecretariavirtual.enums.ConversationState;
import br.com.minhasecretariavirtual.model.Conversation;
import br.com.minhasecretariavirtual.model.ServiceCatalogItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchedulingFlowStrategy implements ConversationFlowStrategy {

    private final FeatureService featureService;
    private final ServiceCatalogService catalogService;
    private final ServiceSelectionParser serviceParser;
    private final DateParser dateParser;

    @Override
    public ConversationResponseDTO nextStep(
            Conversation conversation,
            String message
    ) {

        return switch (conversation.getState()) {

            case NOVO_CONTATO ->
                    handleNewContact(conversation);

            case AGUARDANDO_SERVICO ->
                    handleServiceSelection(conversation, message);

            case AGUARDANDO_DATA ->
                    handleDate(conversation, message);

            case AGUARDANDO_CONFIRMACAO ->
                    handleConfirmation(conversation, message);

            default ->
                    handleEscalation(conversation);
        };
    }

    /* =========================
       HANDLERS DE ESTADO
       ========================= */

    private ConversationResponseDTO handleNewContact(
            Conversation conversation
    ) {
        conversation.setState(ConversationState.AGUARDANDO_SERVICO);
        return askService();
    }

    private ConversationResponseDTO handleServiceSelection(
            Conversation conversation,
            String message
    ) {
        boolean validService =
                serviceParser.isValid(message);

        if (!validService) {
            return askServiceAgain();
        }

        conversation.setState(ConversationState.AGUARDANDO_DATA);
        return askDate();
    }

    private ConversationResponseDTO handleDate(
            Conversation conversation,
            String message
    ) {

        if (!featureService.isAgendaEnabled()) {
            conversation.setState(ConversationState.ESCALADO_HUMANO);
            return forwardToProfessional();
        }

        boolean validDate = dateParser.isValid(message);

        if (!validDate) {
            return askDateAgain();
        }

        conversation.setState(ConversationState.AGUARDANDO_CONFIRMACAO);
        return askConfirmation();
    }

    private ConversationResponseDTO handleConfirmation(
            Conversation conversation,
            String message
    ) {

        if (!"sim".equalsIgnoreCase(message)) {
            return askConfirmationAgain();
        }

        conversation.setState(ConversationState.FINALIZADO);
        return finish();
    }

    private ConversationResponseDTO handleEscalation(
            Conversation conversation
    ) {
        conversation.setState(ConversationState.ESCALADO_HUMANO);
        return escalate();
    }

    /* =========================
       ACTION BUILDERS
       ========================= */

    private ConversationResponseDTO askService() {
        List<String> services = catalogService.getActiveServices()
                .stream()
                .map(ServiceCatalogItem::getName)
                .toList();

        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_SERVICE)
                .context(Map.of("services", services))
                .build();
    }

    private ConversationResponseDTO askServiceAgain() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_SERVICE_AGAIN)
                .context(
                        Map.of(
                                "services",
                                catalogService.getActiveServices()
                                        .stream()
                                        .map(ServiceCatalogItem::getName)
                                        .toList(),
                                "error", "INVALID_SERVICE"
                        )
                )
                .build();
    }

    private ConversationResponseDTO askDate() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_DATE)
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO askDateAgain() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_DATE_AGAIN)
                .context(Map.of("error", "INVALID_DATE"))
                .build();
    }

    private ConversationResponseDTO askConfirmation() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_CONFIRMATION)
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO askConfirmationAgain() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ASK_CONFIRMATION_AGAIN)
                .context(Map.of("error", "INVALID_CONFIRMATION"))
                .build();
    }

    private ConversationResponseDTO finish() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.FINISH)
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO forwardToProfessional() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.FORWARD_TO_PROFESSIONAL)
                .context(Map.of())
                .build();
    }

    private ConversationResponseDTO escalate() {
        return ConversationResponseDTO.builder()
                .action(ConversationAction.ESCALATE_HUMAN)
                .context(Map.of())
                .build();
    }
}
