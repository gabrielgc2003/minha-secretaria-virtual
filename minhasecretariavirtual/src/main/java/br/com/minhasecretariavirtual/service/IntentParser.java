package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.enums.UserIntent;

public interface IntentParser {
    UserIntent parse(String message);
}