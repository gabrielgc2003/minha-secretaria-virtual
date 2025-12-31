package br.com.minhasecretariavirtual.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ApiErrorDTO {

    private String code;
    private String message;
}

