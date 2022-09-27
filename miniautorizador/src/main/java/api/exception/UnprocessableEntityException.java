package api.exception;

import api.model.errors.ErrorModel;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private ErrorModel model;

	public UnprocessableEntityException(ErrorModel model, Throwable cause) {
		super(model.getMessage(), cause);
		this.model = model;
	}

	public UnprocessableEntityException(ErrorModel model) {
		this(model, null);
	}
	
}
