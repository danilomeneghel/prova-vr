package api.exception;

import api.model.errors.CommonErrors;
import api.model.errors.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;

public abstract class BaseModelExceptionHandler {

    @ExceptionHandler(ModelException.class)
    public ResponseEntity<Object> handleModelException(ModelException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getModel(), HttpStatus.resolve(ex.getModel().getStatus()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = ex.getStatus();
        ErrorModel errorModel = new ErrorModel(status.value(), status.value() + "000", ex.getReason());
        return new ResponseEntity<>(errorModel, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorModel errorModel = CommonErrors.INVALID_PARAMETERS;
        errorModel.setDetails("Required request body is missing");
        return new ResponseEntity<>(errorModel, HttpStatus.resolve(errorModel.getStatus()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    protected ResponseEntity<Object> handleValidationExceptions(Exception ex, WebRequest request) {
        String details = extractValidationErrors(ex);
        ErrorModel errorModel = CommonErrors.INVALID_PARAMETERS;
        errorModel.setDetails(details);
        return new ResponseEntity<>(errorModel, HttpStatus.resolve(errorModel.getStatus()));
    }

    private String extractValidationErrors(Exception ex) {
        StringBuilder sb = new StringBuilder();
        Iterator<ObjectError> errorsIterator = getBindingResult(ex).getAllErrors().iterator();
        while (errorsIterator.hasNext()) {
            ObjectError error = errorsIterator.next();
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            sb.append(String.format("%s: %s", field, message));
            if (errorsIterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private org.springframework.validation.BindingResult getBindingResult(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) ex).getBindingResult();
        } else if (ex instanceof BindException) {
            return ((BindException) ex).getBindingResult();
        } else {
            throw new IllegalArgumentException("Unsupported exception type");
        }
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnknownException(Exception ex, WebRequest request) {
        ErrorModel errorModel = CommonErrors.UNEXPECTED_ERROR;
        return new ResponseEntity<>(errorModel, HttpStatus.resolve(errorModel.getStatus()));
    }
}
