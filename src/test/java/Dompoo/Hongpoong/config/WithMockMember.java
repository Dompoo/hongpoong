package Dompoo.Hongpoong.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContext.class)
public @interface WithMockMember {

    String email() default "dompoo@gmail.com";

    String username() default "창근";

    String password() default "1234";

    String role() default "ROLE_USER";
}
