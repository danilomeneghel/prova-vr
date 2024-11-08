package api.model.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartaoErrorsTest {

    @Test
    @DisplayName("Verifica o erro NOT_FOUND")
    void testNotFound() {
        assertEquals("404001", CartaoErrors.NOT_FOUND.getCode());
        assertEquals("Nenhum cartão encontrado.", CartaoErrors.NOT_FOUND.getMessage());
        assertEquals(404, CartaoErrors.NOT_FOUND.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro INVALID_NUMBER_CARD")
    void testInvalidNumberCard() {
        assertEquals("404001", CartaoErrors.INVALID_NUMBER_CARD.getCode());
        assertEquals("Número de cartão inválido", CartaoErrors.INVALID_NUMBER_CARD.getMessage());
        assertEquals(404, CartaoErrors.INVALID_NUMBER_CARD.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro ERROR_CREATING")
    void testErrorCreating() {
        assertEquals("400001", CartaoErrors.ERROR_CREATING.getCode());
        assertEquals("Erro ao criar novo cartão. Tente novamente.", CartaoErrors.ERROR_CREATING.getMessage());
        assertEquals(400, CartaoErrors.ERROR_CREATING.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro CARD_EXISTS")
    void testCardExists() {
        assertEquals("422001", CartaoErrors.CARD_EXISTS.getCode());
        assertEquals("Número do cartão já cadastrado. Tente novamente com outro número.", CartaoErrors.CARD_EXISTS.getMessage());
        assertEquals(422, CartaoErrors.CARD_EXISTS.getStatus());
    }
}
