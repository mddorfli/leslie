package org.leslie.server.jpa.mapping;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ClassDataMapping {

    Class<? extends CustomDataMapping<?, ? extends AbstractFormData, ? extends AbstractTableRowData>> value() default NoMapping.class;
}
