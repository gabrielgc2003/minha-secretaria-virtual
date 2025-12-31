package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.ConversationResponseDTO;
import br.com.minhasecretariavirtual.enums.BusinessType;
import br.com.minhasecretariavirtual.model.Conversation;
import br.com.minhasecretariavirtual.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationFlowService {

    private final TenantService tenantService;
    private final InformationalFlowStrategy informationalFlowStrategy;
    private final SchedulingFlowStrategy schedulingFlowStrategy;

    public ConversationResponseDTO nextStep(Conversation conversation) {

        Tenant tenant = tenantService.getCurrentTenant();

        ConversationFlowStrategy strategy =
                resolveStrategy(tenant.getBusinessType());

        return strategy.nextStep(conversation);
    }

    private ConversationFlowStrategy resolveStrategy(
            BusinessType businessType
    ) {
        return switch (businessType) {
            case INFORMATIONAL -> informationalFlowStrategy;
            case SCHEDULING -> schedulingFlowStrategy;
        };
    }
}
