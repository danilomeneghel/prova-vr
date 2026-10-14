package api.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartaoStatusTest {

    @Test
    @DisplayName("Retorna INATIVO quando o valor for 'INATIVO'")
    void testFromValueInativo() {
        assertEquals(CartaoStatus.INATIVO, CartaoStatus.fromValue("INATIVO"));
    }

    @Test
    @DisplayName("Retorna ATIVO quando o valor for 'ATIVO'")
    void testFromValueAtivo() {
        assertEquals(CartaoStatus.ATIVO, CartaoStatus.fromValue("ATIVO"));
    }

    @Test
    @DisplayName("Retorna null quando o valor não corresponder a nenhum status")
    void testFromValueInvalid() {
        assertNull(CartaoStatus.fromValue("INVALIDO"));
    }
}
