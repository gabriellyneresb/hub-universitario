// apps/backend/src/main/java/br/edu/hub/exception/GlobalExceptionHandler.java
package br.edu.hub.exception;

import br.edu.hub.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tradutor central de exceções da aplicação para respostas HTTP previsíveis, em conformidade
 * com o contrato descrito no {@code PROJECT.md} (seção "Contrato HTTP esperado").
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Validation failed", LocalDateTime.now(), errors));
    }

    /**
     * Traduz a ausência de um recurso (por exemplo, uma atividade inexistente) em
     * {@code 404 Not Found}.
     *
     * <p>Correção do bug P2: antes, um identificador inexistente disparava
     * {@link IllegalArgumentException}, respondida como {@code 500 Internal Server Error},
     * contrariando o contrato do {@code PROJECT.md}. Agora usa {@link ActivityNotFoundException},
     * tratada explicitamente como {@code 404}.</p>
     */
    @ExceptionHandler(ActivityNotFoundException.class)
    ResponseEntity<ErrorResponse> handleActivityNotFound(ActivityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(exception.getMessage()));
    }

    /**
     * Traduz uma tentativa de inscrição em atividade lotada/encerrada em {@code 409 Conflict}
     * (correção do bug P1).
     */
   @ExceptionHandler(ActivityFullException.class)
    ResponseEntity<ErrorResponse> handleActivityFull(ActivityFullException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(exception.getMessage()));
    }

    /**
     * Traduz uma tentativa de inscrição com um e-mail já registrado na mesma atividade
     * em {@code 409 Conflict}.
     */
    @ExceptionHandler(DuplicateRegistrationException.class)
    ResponseEntity<ErrorResponse> handleDuplicateRegistration(DuplicateRegistrationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(exception.getMessage()));
    }

    /**
     * Traduz argumentos inválidos genéricos em {@code 400 Bad Request}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(exception.getMessage()));
    }
}