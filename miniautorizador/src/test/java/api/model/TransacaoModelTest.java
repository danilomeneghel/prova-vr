package api.model;

import api.enums.CartaoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoModelTest {

    @Test
    @DisplayName("Cria o TransacaoModel com valores corretos")
    void testTransacaoModelCreation() {
        CartaoModel cartao = CartaoModel.builder()
                .id(1L)
                .numeroCartao(123456789L)
                .saldo(new SaldoModel(new BigDecimal("100.00")))
                .status(CartaoStatus.ATIVO)
                .build();

        TransacaoModel transacao = TransacaoModel.builder()
                .id(1L)
                .cartao(cartao)
                .valor(new BigDecimal("50.00"))
                .build();

        assertNotNull(transacao);
        assertEquals(1L, transacao.getId());
        assertEquals(cartao, transacao.getCartao());
        assertEquals(new BigDecimal("50.00"), transacao.getValor());
    }
}
