package api.model.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorModelTest {

    @Test
    @DisplayName("Verifica a criação de um ErrorModel")
    void testErrorModel() {
        ErrorModel errorModel = new ErrorModel(400, "400001", "Erro de validação");
        assertEquals(400, errorModel.getStatus());
        assertEquals("400001", errorModel.getCode());
        assertEquals("Erro de validação", errorModel.getMessage());
    }

    @Test
    @DisplayName("Verifica a criação de um ErrorModel com detalhes")
    void testErrorModelWithDetails() {
        ErrorModel errorModel = new ErrorModel(500, "500001", "Erro inesperado", "Detalhes do erro");
        assertEquals(500, errorModel.getStatus());
        assertEquals("500001", errorModel.getCode());
        assertEquals("Erro inesperado", errorModel.getMessage());
        assertEquals("Detalhes do erro", errorModel.getDetails());
    }
}
