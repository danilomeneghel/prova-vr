package api.exception;

import api.model.errors.ErrorModel;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

public class ModelException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private ErrorModel model;

    public ModelException(ErrorModel model, Throwable cause) {
        super(model.getMessage(), cause);
        this.model = model;
    }

    public ModelException(ErrorModel model) {
        this(model, null);
    }

}