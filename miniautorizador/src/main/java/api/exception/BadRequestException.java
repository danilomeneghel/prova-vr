package api.exception;

import api.model.errors.ErrorModel;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private ErrorModel model;

	public BadRequestException(ErrorModel model, Throwable cause) {
		super(model.getMessage(), cause);
		this.model = model;
	}

	public BadRequestException(ErrorModel model) {
		this(model, null);
	}
	
}
