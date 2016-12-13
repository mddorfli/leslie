package org.leslie.server.jpa.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field (or getter) for mapping to (and from) an equivalent field of
 * the same name on a matching FormData object.
 * 
 * @author Marco Dörfliger
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface FormDataMapping {
	/**
	 * @return the name of the field on the FormData this field maps to
	 */
	String name() default "";

	/**
	 * @return true if this field should never be set by the formData.
	 */
	boolean isReadOnly() default false;
}
