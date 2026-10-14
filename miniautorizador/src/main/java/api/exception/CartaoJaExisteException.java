package api.exception;

import api.model.CartaoRespostaModel;
import lombok.Getter;

public class CartaoJaExisteException extends RuntimeException {

    @Getter
    private final CartaoRespostaModel cartaoRespostaModel;

    public CartaoJaExisteException(String numeroCartao, String senha) {
        super("Cartão já existe: " + numeroCartao);
        this.cartaoRespostaModel = CartaoRespostaModel.builder()
                .numeroCartao(numeroCartao)
                .senha(senha)
                .build();
    }
}
