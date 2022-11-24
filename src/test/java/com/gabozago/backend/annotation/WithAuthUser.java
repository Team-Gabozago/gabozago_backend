package com.gabozago.backend.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory.class)
public @interface WithAuthUser {
    String id();
    String role();
    String email() default "test@example.com";
    String nickname() default "nickname";
    String password() default "password";
    String phoneNumber() default "010-1234-5678";
}
