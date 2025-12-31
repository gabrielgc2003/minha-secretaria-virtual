package br.com.minhasecretariavirtual.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequestDTO {

    /**
     * Número do remetente (WhatsApp).
     * Ex: +5511999999999
     */
    @NotBlank(message = "O campo 'from' é obrigatório")
    private String from;

    /**
     * Mensagem enviada pelo usuário.
     */
    @NotBlank(message = "O campo 'message' é obrigatório")
    private String message;
}