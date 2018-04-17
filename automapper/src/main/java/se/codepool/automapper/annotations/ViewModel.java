/**
 * 
 */
package se.codepool.automapper.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * An annotation used to select to which entity will current type be mapped.
 * @author Ali
 */
public @interface ViewModel {
	/**
	 * Entity type to be mapped from
	 * @return
	 * 		Entity type to be mapped
	 */
	Class<?> entity();
}
