package api.model;

import api.enums.CartaoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartaoModelTest {

    @Test
    @DisplayName("Cria o CartaoModel com valores corretos")
    void testCartaoModelCreation() {
        CartaoModel cartao = CartaoModel.builder()
                .id(1L)
                .numeroCartao("6549873025634501")
                .saldo(new BigDecimal("500.00"))
                .status(CartaoStatus.ATIVO)
                .build();

        assertNotNull(cartao);
        assertEquals(1L, cartao.getId());
        assertEquals("6549873025634501", cartao.getNumeroCartao());
        assertEquals(new BigDecimal("500.00"), cartao.getSaldo());
        assertEquals(CartaoStatus.ATIVO, cartao.getStatus());
    }
}
