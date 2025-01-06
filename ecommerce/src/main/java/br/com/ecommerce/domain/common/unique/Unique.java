package br.com.ecommerce.domain.common.unique;

import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.infrastructure.repository.GenericUniqueConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = GenericUniqueConstraintValidator.class)
public @interface Unique {
    Class<? extends GenericEntity> ownerClass();
    String name();
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}