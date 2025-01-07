package br.com.ecommerce.application.common;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@WithSecurityContext(factory = WithMockJwtFactory.class)
public @interface WithMockJwt {
    String subject() default "junit-test";
    String[] roles() default {};
    boolean expired() default false;
}
