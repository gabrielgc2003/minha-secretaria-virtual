package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.n8n.N8nAgentOutput;
import br.com.minhasecretariavirtual.dto.n8n.N8nMessageInput;
import br.com.minhasecretariavirtual.dto.n8n.SpringContextResponse;
import br.com.minhasecretariavirtual.model.Contact;
import br.com.minhasecretariavirtual.model.Message;
import br.com.minhasecretariavirtual.model.Tenant;
import br.com.minhasecretariavirtual.model.enums.MessageRole;
import br.com.minhasecretariavirtual.repository.ContactRepository;
import br.com.minhasecretariavirtual.repository.MessageRepository;
import br.com.minhasecretariavirtual.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Importante
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatFlowService {

    private final TenantRepository tenantRepository;
    private final ContactRepository contactRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public SpringContextResponse processIncomingAndGetContext(String tenantIdStr, N8nMessageInput input) {
        // 1. Converter String para UUID
        UUID tenantId = UUID.fromString(tenantIdStr);
        Optional<Tenant> tenant = tenantRepository.findById(tenantId);
        // 2. Identificar ou Criar o Contato
        Contact contact = contactRepository.findByRemoteJidAndTenantId(input.getRemoteJid(), tenantId)
                .orElseGet(() -> createNewContact(tenantId, input));

        // 3. Salvar a mensagem recebida do usuário
        saveMessage(contact, input.getMessageText(), MessageRole.USER);

        // 4. Recuperar histórico recente (Ex: últimas 10 mensagens) para a IA
        List<String> history = messageRepository.findLatestByContact(contact.getId(), PageRequest.of(0, 10))
                .stream()
                .map(msg -> String.format("%s: %s", msg.getRole(), msg.getContent()))
                .collect(Collectors.toList());

        // Inverte a lista para ordem cronológica (Antiga -> Nova)
        Collections.reverse(history);

        // 5. Montar o Contexto
        return SpringContextResponse.builder()
                .userId(contact.getId())
                .userName(contact.getPushName())
                .conversationState(contact.getConversationState())
                .history(history)
                .prompt(tenant.map(Tenant::getSystemPrompt).orElse(""))
                .instructions(tenant.map(Tenant::getInstruction).orElse(""))
                .build();
    }

    @Transactional
    public void saveAgentResponse(String tenantIdStr, N8nAgentOutput output) {
        UUID tenantId = UUID.fromString(tenantIdStr);

        contactRepository.findByRemoteJidAndTenantId(output.getRemoteJid(), tenantId)
                .ifPresent(contact -> {
                    // 1. Atualiza o Estado se houver mudança
                    if (output.getNewState() != null && !output.getNewState().isBlank()) {
                        contact.setConversationState(output.getNewState());
                        contactRepository.save(contact);
                    }

                    // 2. Salva a resposta da IA no histórico
                    if (output.getIaResponse() != null) {
                        saveMessage(contact, output.getIaResponse(), MessageRole.ASSISTANT);
                    }
                });
    }

    // --- Métodos Auxiliares Privados ---

    private Contact createNewContact(UUID tenantId, N8nMessageInput input) {
        log.info("Novo contato detectado: {}", input.getPushName());

        Contact newContact = Contact.builder()
                .tenantId(tenantId) // Definimos explicitamente pois o BaseEntity exige
                .remoteJid(input.getRemoteJid())
                .pushName(input.getPushName())
                .conversationState("INICIAL")
                .build();

        return contactRepository.save(newContact);
    }

    private void saveMessage(Contact contact, String content, MessageRole role) {
        Message msg = Message.builder()
                .tenantId(contact.getTenantId()) // Propagamos o Tenant do Contato para a Mensagem
                .contact(contact)
                .content(content)
                .role(role)
                .type("text")
                .build();

        messageRepository.save(msg);
    }
}