package br.com.minhasecretariavirtual.exception;

import br.com.minhasecretariavirtual.dto.ApiErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationError(
            MethodArgumentNotValidException ex
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");

        return ResponseEntity
                .badRequest()
                .body(
                        ApiErrorDTO.builder()
                                .code("VALIDATION_ERROR")
                                .message(message)
                                .build()
                );
    };

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorDTO> handleBusinessError(
            IllegalStateException ex
    ) {

        log.warn("Business rule violation: {}", ex.getMessage());

        return ResponseEntity
                .unprocessableEntity()
                .body(
                        ApiErrorDTO.builder()
                                .code("BUSINESS_ERROR")
                                .message(ex.getMessage())
                                .build()
                );
    };

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            EntityNotFoundException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiErrorDTO.builder()
                                .code("NOT_FOUND")
                                .message("Recurso não encontrado")
                                .build()
                );
    };

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericError(
            Exception ex
    ) {

        log.error("Unexpected error", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiErrorDTO.builder()
                                .code("INTERNAL_ERROR")
                                .message(
                                        "Ocorreu um erro inesperado. Tente novamente mais tarde."
                                )
                                .build()
                );
    };

}
