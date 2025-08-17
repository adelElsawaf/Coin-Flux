package com.coinflux.web.auth.annotations;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)   // works on method parameters
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggedInUser {
}