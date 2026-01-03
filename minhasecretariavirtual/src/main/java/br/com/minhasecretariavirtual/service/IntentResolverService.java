package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.enums.UserIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntentResolverService {

    private final IntentParser aiParser;
    private final IntentParser fallbackParser;

    public UserIntent resolve(
            UUID tenantId,
            String message
    ) {

        UserIntent intent;
        boolean usedFallback = false;

        try {
            intent = aiParser.parse(message);

            if (intent == UserIntent.UNKNOWN) {
                usedFallback = true;
                intent = fallbackParser.parse(message);
            }

        } catch (Exception e) {
            usedFallback = true;
            intent = fallbackParser.parse(message);
        }

        logIntent(tenantId, message, intent, usedFallback);

        return intent;
    }

    private void logIntent(
            UUID tenantId,
            String message,
            UserIntent intent,
            boolean fallback
    ) {
        log.info(
                "intent_classified tenant={} message_length={} intent={} fallback={}",
                tenantId,
                message != null ? message.length() : 0,
                intent,
                fallback
        );
    }
}
