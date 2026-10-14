package api.exception;

import lombok.Getter;

public class TransacaoException extends RuntimeException {

    public TransacaoException(String message) {
        super(message);
    }
}
