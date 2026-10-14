package api.model.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransacaoErrorsTest {

    @Test
    @DisplayName("Verifica o erro NOT_FOUND")
    void testNotFound() {
        assertEquals("404001", TransacaoErrors.NOT_FOUND.getCode());
        assertEquals("Nenhuma transação encontrada.", TransacaoErrors.NOT_FOUND.getMessage());
        assertEquals(404, TransacaoErrors.NOT_FOUND.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro INSUFFICIENT_BALANCE")
    void testInsufficientBalance() {
        assertEquals("422001", TransacaoErrors.INSUFFICIENT_BALANCE.getCode());
        assertEquals("SALDO_INSUFICIENTE", TransacaoErrors.INSUFFICIENT_BALANCE.getMessage());
        assertEquals(422, TransacaoErrors.INSUFFICIENT_BALANCE.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro INVALID_PASSWORD")
    void testInvalidPassword() {
        assertEquals("422002", TransacaoErrors.INVALID_PASSWORD.getCode());
        assertEquals("SENHA_INVALIDA", TransacaoErrors.INVALID_PASSWORD.getMessage());
        assertEquals(422, TransacaoErrors.INVALID_PASSWORD.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro INVALID_NUMBER_CARD")
    void testInvalidNumberCard() {
        assertEquals("422003", TransacaoErrors.INVALID_NUMBER_CARD.getCode());
        assertEquals("CARTAO_INEXISTENTE", TransacaoErrors.INVALID_NUMBER_CARD.getMessage());
        assertEquals(422, TransacaoErrors.INVALID_NUMBER_CARD.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro INATIVE_CARD")
    void testInactiveCard() {
        assertEquals("422004", TransacaoErrors.INATIVE_CARD.getCode());
        assertEquals("CARTAO_INEXISTENTE", TransacaoErrors.INATIVE_CARD.getMessage());
        assertEquals(422, TransacaoErrors.INATIVE_CARD.getStatus());
    }
}
