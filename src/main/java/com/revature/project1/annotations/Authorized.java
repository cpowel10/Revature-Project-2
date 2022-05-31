package com.revature.project1.annotations;

import com.revature.project1.model.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Authorized {
    public Role[] allowedRoles() default {};
}
