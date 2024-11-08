package api.entity;

import api.enums.CartaoStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CartaoEntityTest {

    @Test
    @DisplayName("Deve criar uma entidade CartaoEntity com sucesso")
    void testCartaoEntity() {
        SaldoEntity saldoEntity = new SaldoEntity();
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao(1234567890123456L)
                .senha("senha123")
                .saldo(saldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        assertNotNull(cartaoEntity);
        assertEquals(Long.valueOf("1234567890123456"), cartaoEntity.getNumeroCartao());
        assertEquals("senha123", cartaoEntity.getSenha());
        assertEquals(CartaoStatus.ATIVO, cartaoEntity.getStatus());
        assertNotNull(cartaoEntity.getSaldo());
    }

    @Test
    @DisplayName("Deve criar uma entidade CartaoEntity status INATIVO")
    void testCartaoEntityInativo() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao(Long.valueOf("1234567890123456"))
                .status(CartaoStatus.INATIVO)
                .build();

        assertNotNull(cartaoEntity);
        assertNull(cartaoEntity.getSenha());
        assertEquals(CartaoStatus.INATIVO, cartaoEntity.getStatus());
        assertNull(cartaoEntity.getSaldo());
    }
}
