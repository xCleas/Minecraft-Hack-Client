package dev.just.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionAnnotation {
   String name();

   String desc() default "";

   int key() default 0;

   Type type();

   String[] keywords() default {};
}
