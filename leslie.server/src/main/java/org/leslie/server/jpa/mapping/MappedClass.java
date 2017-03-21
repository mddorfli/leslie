package org.leslie.server.jpa.mapping;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

/**
 * Indicates that the annotated class should be considered for data mapping. An optional custom mapping may be specified.
 * 
 * @author mddorfli
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MappedClass {

    Class<? extends CustomDataMapping<?, ? extends AbstractFormData, ? extends AbstractTableRowData>> value() default NoMapping.class;
}
