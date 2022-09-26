package api.model.errors;

import org.springframework.http.HttpStatus;

public class TransacaoErrors {

    public static final ErrorModel NOT_FOUND = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404001", "Nenhuma transação encontrada.");
    public static final ErrorModel INSUFFICIENT_BALANCE = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400001", "Saldo insuficiente para realizar a transação.");
    public static final ErrorModel INVALID_PASSWORD = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400001", "Senha inválida. Tente novamente.");
    public static final ErrorModel INVALID_NUMBER_CARD = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404001", "Número do cartão não encontrado. Tente novamente.");
    public static final ErrorModel INATIVE_CARD = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400001", "Cartão inativo.");
    public static final ErrorModel ERROR_CREATING = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400001", "Erro ao criar nova transação. Tente novamente.");

}