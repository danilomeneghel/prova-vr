package api.model.errors;

import org.springframework.http.HttpStatus;

public class CartaoErrors {

    public static final ErrorModel NOT_FOUND = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404001", "Nenhum cartão encontrado.");
    public static final ErrorModel INVALID_NUMBER_CARD = new ErrorModel(HttpStatus.NOT_FOUND.value(), "404001", "Número de cartão inválido");
    public static final ErrorModel ERROR_CREATING = new ErrorModel(HttpStatus.BAD_REQUEST.value(), "400001", "Erro ao criar novo cartão. Tente novamente.");
    public static final ErrorModel CARD_EXISTS = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY.value(), "422001", "Número do cartão já cadastrado. Tente novamente com outro número.");

}