package br.com.minhasecretariavirtual.dto;

import java.util.List;

public record GrokChatResponse(
        List<Choice> choices
) {
    public record Choice(Message message) {
        public record Message(String content) {}
    }
}
