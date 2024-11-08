package api.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class TransacaoEntityTest {

    @Test
    @DisplayName("Cria uma entidade TransacaoEntity com sucesso")
    void testTransacaoEntity() {
        CartaoEntity cartaoEntity = new CartaoEntity();
        TransacaoEntity transacaoEntity = TransacaoEntity.builder()
                .cartao(cartaoEntity)
                .valor(BigDecimal.valueOf(200))
                .build();

        assertNotNull(transacaoEntity);
        assertEquals(cartaoEntity, transacaoEntity.getCartao());
        assertEquals(BigDecimal.valueOf(200), transacaoEntity.getValor());
    }

    @Test
    @DisplayName("Cria uma entidade TransacaoEntity sem valor especificado")
    void testTransacaoEntitySemValor() {
        CartaoEntity cartaoEntity = new CartaoEntity();
        TransacaoEntity transacaoEntity = TransacaoEntity.builder()
                .cartao(cartaoEntity)
                .build();

        assertNotNull(transacaoEntity);
        assertEquals(cartaoEntity, transacaoEntity.getCartao());
        assertNull(transacaoEntity.getValor());
    }
}
