package com.hanium.product.common;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthRequired {
}
