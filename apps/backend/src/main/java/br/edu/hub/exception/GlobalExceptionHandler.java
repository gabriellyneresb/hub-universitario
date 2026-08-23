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

    /**
     * Traduz falhas de validação de {@code @Valid} em {@code 400 Bad Request}, retornando um
     * mapa de campo/mensagem para orientar a correção do payload.
     *
     * @param exception exceção lançada pelo Bean Validation quando o corpo da requisição é inválido.
     * @return resposta {@code 400} com os erros de campo encontrados.
     */
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
     * <p>Correção do bug P2: essa exceção passou a ter um tratamento dedicado. Antes, o
     * identificador inexistente disparava {@link IllegalArgumentException}, capturada pelo
     * handler abaixo e respondida como {@code 500 Internal Server Error}, contrariando a regra
     * do produto de que "um identificador inexistente é tratado como recurso não encontrado, e
     * não como falha interna do servidor".</p>
     *
     * @param exception exceção que sinaliza que o recurso não foi encontrado.
     * @return resposta {@code 404} com uma mensagem compreensível para o cliente.
     */
    @ExceptionHandler(ActivityNotFoundException.class)
    ResponseEntity<ErrorResponse> handleActivityNotFound(ActivityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(exception.getMessage()));
    }

    /**
     * Traduz argumentos inválidos genéricos em {@code 400 Bad Request}.
     *
     * <p>Este handler existia anteriormente respondendo {@code 500 Internal Server Error}, o
     * que é semanticamente incorreto: um {@link IllegalArgumentException} representa uma entrada
     * inválida fornecida pelo cliente, não uma falha interna do servidor. Ele foi ajustado para
     * {@code 400}, e o caso específico de "atividade não encontrada" passou a usar
     * {@link ActivityNotFoundException}/{@code 404}, tratado pelo handler acima.</p>
     *
     * @param exception exceção de argumento inválido.
     * @return resposta {@code 400} com uma mensagem compreensível para o cliente.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(exception.getMessage()));
    }
}