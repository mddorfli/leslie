package org.leslie.server.jpa.mapping;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldMappingUtility {

    private static final Logger LOG = LoggerFactory.getLogger(FieldMappingUtility.class);

    /**
     * Exports form data into an entity (i.e. writes to the entity).
     * 
     * @param fromFormData
     * @param toEntity
     */
    @SuppressWarnings("unchecked")
    public static void exportFormData(AbstractFormData fromFormData, Object toEntity) {
	for (Class<?> clazz = toEntity.getClass(); clazz
		.isAnnotationPresent(ClassDataMapping.class); clazz = clazz.getSuperclass()) {
	    for (Field field : clazz.getDeclaredFields()) {
		FieldDataMapping annotation = field.getAnnotation(FieldDataMapping.class);
		if (annotation == null || annotation.readOnly() || annotation.ignoreFormData()) {
		    // ignore unmapped, read-only and ignored fields
		    continue;
		}

		String mappedFieldName = annotation.formFieldName().isEmpty() ? field.getName()
			: annotation.formFieldName();
		Object value = getProperty(fromFormData, mappedFieldName, null);
		if (value != null && value instanceof AbstractValueFieldData) {
		    setProperty(toEntity, field.getName(), field.getType(),
			    ((AbstractValueFieldData<?>) value).getValue());
		} else {
		    setProperty(toEntity, field.getName(), field.getType(), value);
		}
	    }
	}

	getCustomMapping(toEntity).ifPresent(customMapping -> customMapping.write(fromFormData, toEntity));
    }

    /**
     * Imports page data from an entity (i.e. reads from an entity).
     * 
     * @param fromEntity
     * @param toFormData
     */
    @SuppressWarnings("unchecked")
    public static void importFormData(Object fromEntity, AbstractFormData toFormData) {
	for (Class<?> clazz = fromEntity.getClass(); clazz
		.isAnnotationPresent(ClassDataMapping.class); clazz = clazz.getSuperclass()) {
	    for (Field field : clazz.getDeclaredFields()) {
		FieldDataMapping annotation = field.getAnnotation(FieldDataMapping.class);
		if (annotation == null || annotation.ignoreFormData()) {
		    // ignore unmapped, read-only and ignored fields
		    continue;
		}

		Object value = getProperty(fromEntity, field.getName(), field.getType());
		String mappedFieldName = annotation.formFieldName().isEmpty() ? field.getName()
			: annotation.formFieldName();
		Object valueField = getProperty(toFormData, mappedFieldName, null);

		if (valueField != null && valueField instanceof AbstractValueFieldData) {
		    ((AbstractValueFieldData<Object>) valueField).setValue(value);
		} else {
		    setProperty(toFormData, mappedFieldName, field.getType(), value);
		}
	    }
	}

	getCustomMapping(fromEntity).ifPresent(customMapper -> customMapper.read(fromEntity, toFormData));
    }

    /**
     * Imports table page data from entities.
     * 
     * @param fromEntities
     * @param toTablePage
     */
    public static void importTablePageData(List<?> fromEntities, AbstractTablePageData toTablePage) {
	importTablePageData(fromEntities, toTablePage, null);
    }

    /**
     * Imports table page data from entities, with an additional custom mapping
     * which is applied on each entity/row mapping.
     * 
     * @param fromEntities
     * @param toTablePage
     * @param customMapping
     */
    public static <E> void importTablePageData(List<E> fromEntities, AbstractTablePageData toTablePage,
	    BiConsumer<E, AbstractTableRowData> customMapping) {
	for (E entity : fromEntities) {
	    AbstractTableRowData row = toTablePage.addRow();
	    importTableRowData(entity, row);
	    if (customMapping != null) {
		customMapping.accept(entity, row);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    public static void importTableRowData(Object fromEntity, AbstractTableRowData toRowData) {
	for (Class<?> clazz = fromEntity.getClass(); clazz
		.isAnnotationPresent(ClassDataMapping.class); clazz = clazz.getSuperclass()) {

	    for (Field field : clazz.getDeclaredFields()) {
		FieldDataMapping annotation = field.getAnnotation(FieldDataMapping.class);
		if (annotation == null || annotation.ignorePageData()) {
		    // ignore unmapped, read-only and ignored fields
		    continue;
		}

		Object value = getProperty(fromEntity, field.getName(), field.getType());
		String mappedFieldName = annotation.pageFieldName().isEmpty() ? field.getName()
			: annotation.formFieldName();
		setProperty(toRowData, mappedFieldName, field.getType(), value);
	    }
	}

	getCustomMapping(fromEntity).ifPresent(customMapper -> customMapper.read(fromEntity, toRowData));
    }

    private static void setProperty(Object object, String fieldName, Class<?> fieldType, Object value) {
	String setterName = "set" + capatalizeFieldName(fieldName);
	try {
	    Class<?> argType = getOverloadedSetterArgType(object.getClass(), setterName, fieldType);
	    if (argType == null) {
		throw new ProcessingException("Unable to discern type of {}.{} for arg {}",
			object.getClass(), fieldName, value);
	    }

	    Object argValue;
	    if (value == null || value.getClass().equals(argType)) {
		argValue = value;
	    } else {
		argValue = castToPrimitiveOrWrapper(value, argType);
	    }

	    Method setter = object.getClass().getMethod(setterName, argType);
	    if (argValue != null || !argType.isPrimitive()) {
		// to prevent setting of null on primitive types
		setter.invoke(object, argValue);
	    }

	} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException e) {
	    LOG.warn("Unable to set value of " + object.getClass() + "." + fieldName + " to " + value, e);
	    throw new ProcessingException("Unable to set value of {}.{} to {}: {}", object.getClass(), fieldName, value,
		    e.getMessage());
	}
    }

    private static Object castToPrimitiveOrWrapper(Object value, Class<?> argType) {
	Object result;
	if (argType.isPrimitive()) {
	    // wrapper types can't be cast to primitives
	    result = getPrimitiveFor(value);
	} else {
	    // primitives can be cast to wrapper types
	    result = argType.cast(value);
	}
	return result;
    }

    private static Class<?> getOverloadedSetterArgType(Class<?> clazz, String setterName, Class<?> argType) {
	Class<?> newType;
	try {
	    clazz.getMethod(setterName, argType);
	    // if no exception occurs, the original type is OK
	    newType = argType;

	} catch (NoSuchMethodException | SecurityException e) {
	    // if an exception occurs, try to find a wrapper/primitive type
	    if (argType.isPrimitive()) {
		newType = getWrapperType(argType);
	    } else {
		newType = getPrimitiveType(argType);
	    }
	}
	return newType;
    }

    private static Class<?> getWrapperType(Class<?> clazz) {
	assert clazz.isPrimitive();
	Class<?> newType;
	switch (clazz.getSimpleName()) {
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
	    throw new ProcessingException("Unknown primitive type {}", clazz);
	}
	return newType;
    }

    private static Class<?> getPrimitiveType(Class<?> clazz) {
	assert !clazz.isPrimitive();
	Class<?> newType;
	switch (clazz.getSimpleName()) {
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
	    throw new ProcessingException("Unknown wrapper type {}", clazz);
	}
	return newType;
    }

    private static Object getPrimitiveFor(Object wrapperValue) {
	assert !wrapperValue.getClass().isPrimitive();
	Object value = null;
	if (wrapperValue instanceof Boolean) {
	    value = ((Boolean) wrapperValue).booleanValue();

	} else if (wrapperValue instanceof Integer) {
	    value = ((Integer) wrapperValue).intValue();

	} else if (wrapperValue instanceof Long) {
	    value = ((Long) wrapperValue).longValue();

	} else if (wrapperValue instanceof Float) {
	    value = ((Float) wrapperValue).floatValue();

	} else if (wrapperValue instanceof Double) {
	    value = ((Double) wrapperValue).doubleValue();

	} else if (wrapperValue instanceof Character) {
	    value = ((Character) wrapperValue).charValue();

	} else if (wrapperValue instanceof Byte) {
	    value = ((Byte) wrapperValue).byteValue();

	} else {
	    throw new ProcessingException("Unknown wrapper type {}", wrapperValue.getClass());
	}
	return value;
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

    @SuppressWarnings("rawtypes")
    private static Optional<CustomDataMapping> getCustomMapping(Object entity) {
	CustomDataMapping customMapping = null;
	ClassDataMapping classMapping = entity.getClass().getAnnotation(ClassDataMapping.class);
	if (classMapping != null) {
	    try {
		customMapping = classMapping.value().newInstance();
	    } catch (InstantiationException | IllegalAccessException e) {
		LOG.error("Unable to instantiate " + classMapping.value(), e);
		throw new ProcessingException("Unable to instantiate {}: {}", classMapping.value(),
			e.getMessage());
	    }
	}
	return Optional.ofNullable(customMapping);
    }
}
