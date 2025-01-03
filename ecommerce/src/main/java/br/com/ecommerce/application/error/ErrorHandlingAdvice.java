package br.com.ecommerce.application.error;

import br.com.ecommerce.application.common.ErrorDescription;
import br.com.ecommerce.application.common.Errors;
import br.com.ecommerce.util.Translator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandlingAdvice extends ResponseEntityExceptionHandler {
    private final Translator translator;

    public ErrorHandlingAdvice(Translator translator) {
        this.translator = translator;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Errors errors = getErrors(ex.getBindingResult());
        return super.handleExceptionInternal(ex, errors, headers, status, request);
    }

    Errors getErrors(BindingResult bindingResult) {
        Errors errors = new Errors();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .forEach(objectError -> {
                        ErrorDescription errorDescription = new ErrorDescription(((FieldError) objectError).getField(), objectError.getDefaultMessage(), translator.translate(objectError.getDefaultMessage()));
                        errors.addError(errorDescription);
                    });
        }
        return errors;
    }
}