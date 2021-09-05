package myAdmin.core.authority;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 权限注解
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Authentication {
	String value();
}
