package api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriaCartaoModelTest {

    @Test
    @DisplayName("Cria o CriaCartaoModel com valores corretos")
    void testCriaCartaoModelCreation() {
        CriaCartaoModel criaCartao = CriaCartaoModel.builder()
                .numeroCartao(123456789L)
                .senha("1234")
                .build();

        assertNotNull(criaCartao);
        assertEquals(123456789L, criaCartao.getNumeroCartao());
        assertEquals("1234", criaCartao.getSenha());
    }
}
