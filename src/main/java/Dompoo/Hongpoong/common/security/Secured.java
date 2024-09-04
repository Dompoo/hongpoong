package Dompoo.Hongpoong.common.security;

public @interface Secured {
	SecurePolicy value() default SecurePolicy.ALL_MEMBER;
}
