package api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CriaTransacaoModelTest {

    @Test
    @DisplayName("Cria o CriaTransacaoModel com valores corretos - campo senhaCartao")
    void testCriaTransacaoModelCreation() {
        CriaTransacaoModel criaTransacao = CriaTransacaoModel.builder()
                .numeroCartao("6549873025634501")
                .senhaCartao("1234")
                .valor(new BigDecimal("50.00"))
                .build();

        assertNotNull(criaTransacao);
        assertEquals("6549873025634501", criaTransacao.getNumeroCartao());
        assertEquals("1234", criaTransacao.getSenhaCartao());
        assertEquals(new BigDecimal("50.00"), criaTransacao.getValor());
    }
}
