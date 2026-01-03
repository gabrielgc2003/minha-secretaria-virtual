package br.com.minhasecretariavirtual.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleServiceSelectionParser implements ServiceSelectionParser {

    private final ServiceCatalogService catalogService;

    @Override
    public boolean isValid(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }

        String normalized = message.trim().toLowerCase();

        return catalogService.getActiveServices()
                .stream()
                .anyMatch(service ->
                        service.getName().toLowerCase().equals(normalized)
                );
    }
}

