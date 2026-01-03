package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.enums.UserIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiIntentParser implements IntentParser {

    private final AiClient aiClient;

    @Override
    public UserIntent parse(String message) {

        if (message == null || message.isBlank()) {
            return UserIntent.UNKNOWN;
        }

        return aiClient.classifyIntent(message);
    }
}

