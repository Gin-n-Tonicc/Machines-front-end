package com.machines.machines_front_end.annotations;

import org.springframework.lang.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageRoleGuard {
    boolean authenticated();
    String redirectTo();
    String role() default "";
}
