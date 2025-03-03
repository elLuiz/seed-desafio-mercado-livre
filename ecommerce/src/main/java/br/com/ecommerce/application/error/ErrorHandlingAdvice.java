package br.com.ecommerce.application.error;

import br.com.ecommerce.application.common.ErrorDescription;
import br.com.ecommerce.application.common.Errors;
import br.com.ecommerce.domain.exception.DomainException;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.util.Translator;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
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

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Errors> handleValidationException(ValidationException validationException) {
        Errors errors = new Errors();
        validationException.getErrors().getErrors().forEach(validationError -> errors.addError(new ErrorDescription(validationError.field(), validationError.code(), translator.translate(validationError.code()))));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoResultException.class)
    protected ResponseEntity<Errors> handleNoResultException(NoResultException noResultException) {
        log.warn("No result found for query", noResultException);
        Errors errors = new Errors();
        errors.addError(new ErrorDescription(null, "resource.not.found", translator.translate("resource.not.found")));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(DomainException.class)
    protected ResponseEntity<Errors> handleNoResultException(DomainException domainException) {
        log.warn("Domain exception", domainException);
        Errors errors = new Errors();
        errors.addError(new ErrorDescription(null, domainException.getMessage(), translator.translate(domainException.getMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}