// apps/backend/src/main/java/br/edu/hub/exception/ActivityNotFoundException.java
package br.edu.hub.exception;

/**
 * Exceção lançada quando uma {@code Activity} não é encontrada pelo identificador informado.
 *
 * <p>Esta exceção é tratada pelo {@link GlobalExceptionHandler}, que a traduz para uma
 * resposta HTTP {@code 404 Not Found}, em conformidade com o contrato descrito no
 * {@code PROJECT.md} ("Um identificador inexistente é tratado como recurso não encontrado,
 * e não como falha interna do servidor").</p>
 */
public class ActivityNotFoundException extends RuntimeException {

    /**
     * Cria a exceção com a mensagem que será exposta no corpo da resposta de erro.
     *
     * @param message mensagem compreensível para o cliente da API, sem detalhes internos.
     */
    public ActivityNotFoundException(String message) {
        super(message);
    }
}