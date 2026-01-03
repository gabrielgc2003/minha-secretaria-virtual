package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.enums.UserIntent;
import org.springframework.stereotype.Service;

@Service
public class SimpleIntentParser {

    public UserIntent parse(String message) {
        if (message == null) return UserIntent.UNKNOWN;

        String normalized = message.toLowerCase();

        if (normalized.contains("info")) return UserIntent.INFORMATION;
        if (normalized.contains("atendente")) return UserIntent.HUMAN;

        return UserIntent.UNKNOWN;
    }
}
