package api.model.errors;

import org.springframework.http.HttpStatus;

public class TransacaoErrors {

    public static final ErrorModel NOT_FOUND = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404001", "Nenhuma transação encontrada.");
    public static final ErrorModel INSUFFICIENT_BALANCE = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422001", "SALDO_INSUFICIENTE");
    public static final ErrorModel INVALID_PASSWORD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422002", "SENHA_INVALIDA");
    public static final ErrorModel INVALID_NUMBER_CARD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422003", "CARTAO_INEXISTENTE");
    public static final ErrorModel INATIVE_CARD = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422004", "CARTAO_INEXISTENTE");

}