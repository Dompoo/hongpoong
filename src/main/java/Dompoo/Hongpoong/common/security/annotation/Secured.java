package Dompoo.Hongpoong.common.security.annotation;

import Dompoo.Hongpoong.common.security.SecurePolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Secured {
	SecurePolicy value() default SecurePolicy.ALL_MEMBER;
}
