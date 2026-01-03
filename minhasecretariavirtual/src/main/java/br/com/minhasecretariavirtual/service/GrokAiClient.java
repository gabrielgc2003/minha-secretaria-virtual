package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.dto.GrokChatRequest;
import br.com.minhasecretariavirtual.dto.GrokChatResponse;
import br.com.minhasecretariavirtual.enums.UserIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class GrokAiClient implements AiClient {

    private final RestClient grokRestClient;

    @Value("${grok.api.url}")
    private String apiUrl;

    @Value("${grok.api.key}")
    private String apiKey;

    @Value("${ai.intent.model}")
    private String model;

    @Override
    public UserIntent classifyIntent(String message) {

        if (message == null || message.isBlank()) {
            return UserIntent.UNKNOWN;
        }

        try {
            GrokChatRequest request = buildRequest(message);

            GrokChatResponse response =
                    grokRestClient.post()
                            .uri(apiUrl)
                            .header("Authorization", "Bearer " + apiKey)
                            .body(request)
                            .retrieve()
                            .body(GrokChatResponse.class);

            return extractIntent(response);

        } catch (Exception e) {
            // IMPORTANTE: nunca quebrar o fluxo
            return UserIntent.UNKNOWN;
        }
    }

    private GrokChatRequest buildRequest(String message) {

        String prompt = """
            Você é um classificador de intenção.
            Analise a mensagem abaixo e identifique a intenção principal do usuário.
            
            Responda SOMENTE com UMA das opções abaixo (sem explicações):
            - INFORMATION  → quando o usuário busca informações gerais (horário, endereço, serviços, preços, dúvidas).
            - HUMAN        → quando o usuário quer falar com um atendente humano ou pessoa real.
            - UNKNOWN      → quando não for possível identificar claramente a intenção.
            
            Mensagem do usuário:
            "%s"
            """.formatted(message);

        return new GrokChatRequest(
                model,
                List.of(
                        new GrokChatRequest.Message("user", prompt)
                ),
                0.0,
                5
        );
    }

    private UserIntent extractIntent(GrokChatResponse response) {

        if (response == null ||
                response.choices() == null ||
                response.choices().isEmpty()) {
            return UserIntent.UNKNOWN;
        }

        String raw =
                response.choices()
                        .get(0)
                        .message()
                        .content()
                        .trim()
                        .toUpperCase();

        try {
            return UserIntent.valueOf(raw);
        } catch (IllegalArgumentException e) {
            return UserIntent.UNKNOWN;
        }
    }
}

