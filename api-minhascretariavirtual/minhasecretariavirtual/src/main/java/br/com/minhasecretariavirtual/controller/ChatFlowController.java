package br.com.minhasecretariavirtual.controller;

import br.com.minhasecretariavirtual.dto.n8n.N8nAgentOutput;
import br.com.minhasecretariavirtual.dto.n8n.N8nMessageInput;
import br.com.minhasecretariavirtual.dto.n8n.SpringContextResponse;
import br.com.minhasecretariavirtual.service.ChatFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat-flow")
@RequiredArgsConstructor
public class ChatFlowController {

    private final ChatFlowService chatFlowService;

    // Passo 2 e 3: Recebe mensagem do n8n e devolve contexto
    @PostMapping("/{tenantId}/start-processing")
    public ResponseEntity<SpringContextResponse> startProcessing(
            @PathVariable String tenantId,
            @RequestBody N8nMessageInput input
    ) {
        SpringContextResponse response = chatFlowService.processIncomingAndGetContext(tenantId, input);
        return ResponseEntity.ok(response);
    }

    // Passo 6: Recebe o resultado da IA para salvar e atualizar estado
    @PostMapping("/{tenantId}/update-intent")
    public ResponseEntity<Void> updateIntent(
            @PathVariable String tenantId,
            @RequestBody N8nAgentOutput output
    ) {
        chatFlowService.saveAgentResponse(tenantId, output);
        return ResponseEntity.ok().build();
    }
}
