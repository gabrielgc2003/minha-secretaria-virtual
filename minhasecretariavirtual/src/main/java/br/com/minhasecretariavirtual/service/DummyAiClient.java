package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.enums.UserIntent;
import org.springframework.stereotype.Service;

@Service
public class DummyAiClient implements AiClient {

    @Override
    public UserIntent classifyIntent(String message) {
        return UserIntent.UNKNOWN;
    }
}

