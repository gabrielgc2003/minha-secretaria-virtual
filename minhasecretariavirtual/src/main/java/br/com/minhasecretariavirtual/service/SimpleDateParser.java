package br.com.minhasecretariavirtual.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SimpleDateParser implements DateParser {

    @Override
    public boolean isValid(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }

        try {
            LocalDate date = parseDate(message);
            return !date.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private LocalDate parseDate(String message) {
        // Exemplo simples: 10/01/2026
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(message.trim(), formatter);
    }
}
