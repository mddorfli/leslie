package org.leslie.server.jpa.mapping;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldMapper {

    private static final Logger LOG = LoggerFactory.getLogger(FieldMapper.class);

    /**
     * Exports form data into an entity (i.e. writes to the entity).
     * 
     * @param fromFormData
     * @param toEntity
     */
    public static void exportFormData(AbstractFormData fromFormData, Object toEntity) {
	for (Field field : toEntity.getClass().getDeclaredFields()) {
	    FieldMapping annotation = field.getAnnotation(FieldMapping.class);
	    if (annotation == null || annotation.readOnly() || annotation.ignoreFormData()) {
		// ignore unmapped, read-only and ignored fields
		continue;
	    }

	    if (!NoMapping.class.equals(annotation.customMapping())) {
		// apply custom mapping
		try {
		    ICustomMapping customMapper = annotation.customMapping().newInstance();
		    customMapper.write(fromFormData, toEntity);
		} catch (InstantiationException | IllegalAccessException e) {
		    LOG.error("Unable to instantiate " + annotation.customMapping(), e);
		    throw new ProcessingException("Unable to instantiate {}: {}", annotation.customMapping(),
			    e.getMessage());
		}
		continue;
	    }

	    String mappedFieldName = annotation.formDataName().isEmpty() ? field.getName() : annotation.formDataName();
	    Object value = getProperty(fromFormData, mappedFieldName, null);
	    if (value != null && value instanceof AbstractValueFieldData) {
		setProperty(toEntity, field.getName(), field.getType(), ((AbstractValueFieldData<?>) value).getValue());
	    } else {
		setProperty(toEntity, field.getName(), field.getType(), value);
	    }
	}
    }

    /**
     * Imports page data from an entity (i.e. reads from an entity).
     * 
     * @param fromEntity
     * @param toFormData
     */
    @SuppressWarnings("unchecked")
    public static void importFormData(Object fromEntity, AbstractFormData toFormData) {
	for (Field field : fromEntity.getClass().getDeclaredFields()) {
	    FieldMapping annotation = field.getAnnotation(FieldMapping.class);
	    if (annotation == null || annotation.ignoreFormData()) {
		// ignore unmapped, read-only and ignored fields
		continue;
	    }

	    if (!NoMapping.class.equals(annotation.customMapping())) {
		// apply custom mapping
		try {
		    ICustomMapping customMapper = annotation.customMapping().newInstance();
		    customMapper.read(fromEntity, toFormData);
		} catch (InstantiationException | IllegalAccessException e) {
		    LOG.error("Unable to instantiate " + annotation.customMapping(), e);
		    throw new ProcessingException("Unable to instantiate {}: {}", annotation.customMapping(),
			    e.getMessage());
		}
		continue;
	    }

	    Object value = getProperty(fromEntity, field.getName(), field.getType());
	    String mappedFieldName = annotation.formDataName().isEmpty() ? field.getName() : annotation.formDataName();
	    Object valueField = getProperty(toFormData, mappedFieldName, null);

	    if (valueField != null && valueField instanceof AbstractValueFieldData) {
		((AbstractValueFieldData<Object>) valueField).setValue(value);
	    } else {
		setProperty(toFormData, mappedFieldName, field.getType(), value);
	    }
	}
    }

    public static void importTablePageData(List<?> fromEntities, AbstractTablePageData toTablePage) {
	for (Object entity : fromEntities) {
	    AbstractTableRowData row = toTablePage.addRow();
	    importTableRowData(entity, row);
	}
    }

    private static void importTableRowData(Object fromEntity, AbstractTableRowData toRowData) {
	for (Field field : fromEntity.getClass().getDeclaredFields()) {
	    FieldMapping annotation = field.getAnnotation(FieldMapping.class);
	    if (annotation == null || annotation.ignorePageData()) {
		// ignore unmapped, read-only and ignored fields
		continue;
	    }

	    if (!NoMapping.class.equals(annotation.customMapping())) {
		// apply custom mapping
		try {
		    ICustomMapping customMapper = annotation.customMapping().newInstance();
		    customMapper.read(fromEntity, toRowData);
		} catch (InstantiationException | IllegalAccessException e) {
		    LOG.error("Unable to instantiate " + annotation.customMapping(), e);
		    throw new ProcessingException("Unable to instantiate {}: {}", annotation.customMapping(),
			    e.getMessage());
		}
		continue;
	    }

	    Object value = getProperty(fromEntity, field.getName(), field.getType());
	    String mappedFieldName = annotation.pageDataName().isEmpty() ? field.getName() : annotation.formDataName();
	    setProperty(toRowData, mappedFieldName, field.getType(), value);
	}
    }

    /**
     * Exports page data into an entity (i.e. writes to an entity).
     * 
     * @param fromTableRow
     * @param toEntity
     */
    private static void exportTableRowData(AbstractTableRowData fromTableRow, Object toEntity) {
	for (Field field : toEntity.getClass().getDeclaredFields()) {
	    FieldMapping annotation = field.getAnnotation(FieldMapping.class);
	    if (annotation == null || annotation.readOnly() || annotation.ignorePageData()) {
		// ignore unmapped, read-only and ignored fields
		continue;
	    }

	    if (!NoMapping.class.equals(annotation.customMapping())) {
		// apply custom mapping
		try {
		    ICustomMapping customMapper = annotation.customMapping().newInstance();
		    customMapper.write(fromTableRow, toEntity);
		} catch (InstantiationException | IllegalAccessException e) {
		    LOG.error("Unable to instantiate " + annotation.customMapping(), e);
		    throw new ProcessingException("Unable to instantiate {}: {}", annotation.customMapping(),
			    e.getMessage());
		}
		continue;
	    }

	    String mappedFieldName = annotation.formDataName().isEmpty() ? field.getName() : annotation.formDataName();
	    Object value = getProperty(fromTableRow, mappedFieldName, field.getType());
	    setProperty(toEntity, field.getName(), field.getType(), value);
	}
    }

    private static void setProperty(Object object, String fieldName, Class<?> fieldType, Object value) {
	String setterName = "set" + capatalizeFieldName(fieldName);
	try {
	    Method setter = getSetter(object, fieldType, setterName);

	    if (value != null || !fieldType.isPrimitive()) {
		setter.invoke(object, value);
	    }

	} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException e) {
	    LOG.warn("Unable to set value of " + object.getClass() + "." + fieldName + " to " + value, e);
	    throw new ProcessingException("Unable to set value of {}.{} to {}: {}", object.getClass(), fieldName, value,
		    e.getMessage());
	}
    }

    private static Method getSetter(Object object, Class<?> fieldType, String setterName) throws NoSuchMethodException {
	try {
	    return object.getClass().getMethod(setterName, fieldType);
	} catch (NoSuchMethodException e) {
	    Class<?> newType;
	    if (fieldType.isPrimitive()) {
		// use the wrapper class
		switch (fieldType.getSimpleName()) {
		case "boolean":
		    newType = Boolean.class;
		    break;
		case "int":
		    newType = Integer.class;
		    break;
		case "long":
		    newType = Long.class;
		    break;
		case "double":
		    newType = Double.class;
		    break;
		case "float":
		    newType = Float.class;
		    break;
		case "char":
		    newType = Character.class;
		    break;
		case "byte":
		    newType = Byte.class;
		    break;
		default:
		    throw new ProcessingException("Unknown primitive type {}", fieldType);
		}
	    } else {
		// use the primitive class
		switch (fieldType.getSimpleName()) {
		case "Boolean":
		    newType = boolean.class;
		    break;
		case "Integer":
		    newType = int.class;
		    break;
		case "Long":
		    newType = long.class;
		    break;
		case "Double":
		    newType = double.class;
		    break;
		case "Float":
		    newType = float.class;
		    break;
		case "Character":
		    newType = char.class;
		    break;
		case "Byte":
		    newType = byte.class;
		    break;
		default:
		    throw new ProcessingException("Unknown wrapper type {}", fieldType);
		}
	    }

	    return object.getClass().getMethod(setterName, newType);
	}
    }

    private static Object getProperty(Object object, String fieldName, Class<?> fieldType) {
	try {
	    String getterPrefix = (fieldType != null
		    && (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) ? "is" : "get");
	    Method getter = object.getClass().getMethod(getterPrefix + capatalizeFieldName(fieldName));
	    return getter.invoke(object);

	} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException e) {
	    LOG.warn("Unable to find getter for " + object.getClass().getName() + "." + fieldName, e);
	    throw new ProcessingException("Unable to find getter for {}.{}: {}", object.getClass().getName(),
		    fieldName, e.getMessage());
	}
    }

    /**
     * Capitalizes the field name unless one of the first two characters are
     * uppercase. This is in accordance with java bean naming conventions in
     * JavaBeans API spec section 8.8.
     *
     * @param fieldName
     * @return the capitalised field name
     * @see Introspector#decapitalize(String)
     */
    private static String capatalizeFieldName(String fieldName) {
	final String result;
	if (fieldName != null && !fieldName.isEmpty()
		&& Character.isLowerCase(fieldName.charAt(0))
		&& (fieldName.length() == 1 || Character.isLowerCase(fieldName.charAt(1)))) {
	    result = Character.toUpperCase(fieldName.charAt(0))
		    + (fieldName.length() == 1 ? "" : fieldName.substring(1));
	} else {
	    result = fieldName;
	}
	return result;
    }
}
