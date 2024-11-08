package api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CriaTransacaoModelTest {

    @Test
    @DisplayName("Cria o CriaTransacaoModel com valores corretos")
    void testCriaTransacaoModelCreation() {
        CriaTransacaoModel criaTransacao = CriaTransacaoModel.builder()
                .numeroCartao(123456789L)
                .senha("1234")
                .valor(new BigDecimal("50.00"))
                .build();

        assertNotNull(criaTransacao);
        assertEquals(123456789L, criaTransacao.getNumeroCartao());
        assertEquals("1234", criaTransacao.getSenha());
        assertEquals(new BigDecimal("50.00"), criaTransacao.getValor());
    }
}
