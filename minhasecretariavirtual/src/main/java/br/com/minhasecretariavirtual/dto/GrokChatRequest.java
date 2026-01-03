package br.com.minhasecretariavirtual.dto;

import java.util.List;

public record GrokChatRequest(
        String model,
        List<Message> messages,
        double temperature,
        int max_tokens
) {
    public record Message(String role, String content) {}
}
