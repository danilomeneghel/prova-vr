package api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SaldoModelTest {

    @Test
    @DisplayName("Cria o SaldoModel com valor correto")
    void testSaldoModelCreation() {
        SaldoModel saldo = new SaldoModel(new BigDecimal("100.00"));

        assertNotNull(saldo);
        assertEquals(new BigDecimal("100.00"), saldo.getValor());
    }
}
