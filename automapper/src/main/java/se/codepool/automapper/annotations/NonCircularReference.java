/**
 * 
 */
package se.codepool.automapper.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * An annotation used to remove circular reference.
 * @author Ali
 */
public @interface NonCircularReference {}
