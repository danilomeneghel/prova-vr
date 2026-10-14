package api.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class SaldoEntityTest {

    @Test
    @DisplayName("Cria uma entidade SaldoEntity com valor especificado")
    void testSaldoEntity() {
        SaldoEntity saldoEntity = SaldoEntity.builder()
                .valor(BigDecimal.valueOf(1000))
                .build();

        assertNotNull(saldoEntity);
        assertEquals(BigDecimal.valueOf(1000), saldoEntity.getValor());
    }

    @Test
    @DisplayName("Cria uma entidade SaldoEntity com valor padrão de R$500")
    void testSaldoEntityValorPadrao() {
        SaldoEntity saldoEntity = new SaldoEntity();
        assertEquals(BigDecimal.valueOf(500), saldoEntity.getValor());
    }
}
