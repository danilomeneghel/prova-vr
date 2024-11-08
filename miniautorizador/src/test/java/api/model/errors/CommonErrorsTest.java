package api.model.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonErrorsTest {

    @Test
    @DisplayName("Verifica o erro INVALID_PARAMETERS")
    void testInvalidParameters() {
        assertEquals("400000", CommonErrors.INVALID_PARAMETERS.getCode());
        assertEquals("Parâmetro inválido", CommonErrors.INVALID_PARAMETERS.getMessage());
        assertEquals(400, CommonErrors.INVALID_PARAMETERS.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro UNAUTHORIZED")
    void testUnauthorized() {
        assertEquals("401000", CommonErrors.UNAUTHORIZED.getCode());
        assertEquals("Não autorizado", CommonErrors.UNAUTHORIZED.getMessage());
        assertEquals(401, CommonErrors.UNAUTHORIZED.getStatus());
    }

    @Test
    @DisplayName("Verifica o erro UNEXPECTED_ERROR")
    void testUnexpectedError() {
        assertEquals("500000", CommonErrors.UNEXPECTED_ERROR.getCode());
        assertEquals("Erro inesperado", CommonErrors.UNEXPECTED_ERROR.getMessage());
        assertEquals(500, CommonErrors.UNEXPECTED_ERROR.getStatus());
    }
}
