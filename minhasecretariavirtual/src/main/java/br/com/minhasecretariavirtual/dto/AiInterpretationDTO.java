package br.com.minhasecretariavirtual.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiInterpretationDTO {

    /**
     * Intenção detectada na mensagem.
     * Ex: SELECT_SERVICE, CONFIRM, ESCALATE
     */
    @NotBlank
    private String intent;

    /**
     * Serviço escolhido, se houver.
     */
    private String service;

    /**
     * Data desejada (ISO).
     */
    private LocalDate date;

    /**
     * Período desejado.
     * Ex: morning, afternoon
     */
    private String period;
}
