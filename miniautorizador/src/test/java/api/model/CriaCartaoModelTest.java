package api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriaCartaoModelTest {

    @Test
    @DisplayName("Cria o CriaCartaoModel com valores corretos")
    void testCriaCartaoModelCreation() {
        CriaCartaoModel criaCartao = CriaCartaoModel.builder()
                .numeroCartao("6549873025634501")
                .senha("1234")
                .build();

        assertNotNull(criaCartao);
        assertEquals("6549873025634501", criaCartao.getNumeroCartao());
        assertEquals("1234", criaCartao.getSenha());
    }
}
