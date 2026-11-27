package com.bibliot.gestion_biblioteca.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ArchivoUrlValidaValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArchivoUrlValida {
    String message() default "Debe ser una URL HTTP/HTTPS o una ruta PDF local válida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
