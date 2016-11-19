package com.java.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.UnsupportedDataTypeException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * Just support class or static inner class
 *
 */
public final class DataUtils {

	/*
	if (fieldType.isAssignableFrom(List.class) || fieldType.isAssignableFrom(Set.class)) {
					field.setAccessible(true);
					Collection<?> coll = (Collection<?>) field.get(target);
					if (coll != null && !coll.isEmpty()) {
						Iterator<?> itr = coll.iterator();
						while (itr.hasNext()) {
							Object item = itr.next();
							getData(item);
						}
					}
				} else if (isHostPortBindable(fieldType)) {
					field.setAccessible(true);
					Object fieldValue = field.get(target);
					getData(fieldValue);
				} else if (fieldName.startsWith("_")) {
					field.setAccessible(true);
					if (fieldName.endsWith("Uris")) {
						if (Collection.class.isAssignableFrom(field.getType())) {
							List<String> completeUris = new ArrayList<>();
							Collection<?> coll = (Collection<?>) field.get(target);
							Iterator<?> itr = coll.iterator();
							while (itr.hasNext()) {
								Object item = itr.next();
								String originalUri = item.toString();
								String serviceName = ExternalHostPortGetterUtils.getServiceNameOfUri(originalUri);
								String externalHostPort = ExternalHostPortGetterUtils.getExternalServiceHostAndPort(serviceName);
								completeUris.add(Constants.HTTP_PROTOCOL+ externalHostPort + originalUri);
							}
							field.set(target, completeUris);
						}
					} else if (fieldName.endsWith("Uri")) {
						String completeUri = buildCompleteUri(target, field);
						field.set(target, completeUri);
					}
				}	 
		*/
	
	public static <T> T getData(Class<T> targetClass) {
		try {
			T target = targetClass.newInstance();
			Field[] fields = getAllFields(targetClass);
			
			for (Field field : fields) {
				String fieldName = field.getName();
				Class<?> fieldType = field.getType();
//				field.set(target, fieldName);
				PropertyUtils.setProperty(target, fieldName, createNewInstance(fieldName, fieldType));
			}
			
			return target;
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static Object createNewInstance(String fieldName, Class<?> fieldType) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object result = null;
		if (fieldType.isPrimitive()) {
			result = getDefaultValueOfPrimitiveType(fieldType);
		} else if (isNumericType(fieldType)) {
			result = fieldType.getConstructor(String.class).newInstance("1");
		} else if (isBooleanType(fieldType)) {
			result = fieldType.getConstructor(String.class).newInstance("true");
		} else if (isDateTime(fieldType)) {
			result = null;
		} else if(isStringOrCharType(fieldType)) {
			result = fieldType.getConstructor(String.class).newInstance(fieldName);
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			if (fieldType.isAssignableFrom(List.class)) {
				result = Collections.EMPTY_LIST;
			} else if (fieldType.isAssignableFrom(Set.class)) {
				result = Collections.EMPTY_SET;
			}
			
			/*if (fieldType) {
				int n = 0;
				while (n < 5) {
					result = ((Collection) result).add(createNewInstance(fieldName, fieldType));
					n++;
				}
			}*/
		} else if (Map.class.isAssignableFrom(fieldType)) {
			result = Collections.EMPTY_MAP;
		} else {
			try {
				result = fieldType.getConstructor(String.class).newInstance(fieldName);
			} catch (Exception e) {
				throw new IllegalArgumentException();
			}
		}
		
		return result;
	}
	
	static Object getDefaultValueOfPrimitiveType(Class<?> fieldType) {
		// TODO Auto-generated method stub
		return PrimitiveDefaults.getDefaultValue(fieldType);
	}

	static boolean isNumericType(Class type) {
		return type.isPrimitive()
                || type == Double.class
                || type == Float.class
                || type == Long.class
                || type == Integer.class
                || type == Short.class;
	}
	
	static boolean isDateTime(Class type) {
		return type.isPrimitive()
                || type == Date.class
                || type == LocalDate.class
                || type == LocalTime.class
                || type == LocalDateTime.class;
	}
	
	static boolean isBooleanType(Class type) {
		return type == Boolean.class;
	}
	
	static boolean isStringOrCharType(Class type) {
		return type == Character.class
                || type == String.class;
	}
	
	/**
	 * Return a list of all fields (whatever access status, and on whatever
	 * superclass they were defined) that can be found on this class. This is
	 * like a union of {@link Class#getDeclaredFields()} which ignores and
	 * super-classes, and {@link Class#getFields()} which ignored non-public
	 * fields
	 *
	 * @param clazz
	 *            The class to introspect
	 * @return The complete list of fields
	 */
	private static Field[] getAllFields(Class<?> clazz) {
		List<Class<?>> classes = getAllSuperclasses(clazz);
		classes.add(clazz);
		return getAllFields(classes);
	}

	/**
	 * As {@link #getAllFields(Class)} but acts on a list of {@link Class}s and
	 * uses only {@link Class#getDeclaredFields()}.
	 *
	 * @param classes
	 *            The list of classes to reflect on
	 * @return The complete list of fields
	 */
	private static Field[] getAllFields(List<Class<?>> classes) {
		Set<Field> fields = new HashSet<Field>();
		for (Class<?> clazz : classes) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}

		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * Return a List of super-classes for the given class.
	 *
	 * @param clazz
	 *            the class to look up
	 * @return the List of super-classes in order going up from this one
	 */
	public static List<Class<?>> getAllSuperclasses(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();

		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}

		return classes;
	}
	
}
