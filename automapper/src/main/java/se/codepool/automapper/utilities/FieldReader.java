package se.codepool.automapper.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import se.codepool.automapper.annotations.MapFrom;
import se.codepool.automapper.annotations.MapTo;
import se.codepool.automapper.annotations.NonCircularReference;
import se.codepool.automapper.exceptions.FieldNotFoundException;
import se.codepool.automapper.exceptions.InvalidAnnotationException;
import se.codepool.automapper.exceptions.InvalidArgumentOnAnnotationException;

/**
 * A class used to read fields, find annotated field and non-annotated fields. Maps values from 
 * the Entity to the ViewModel etc ..
 * @author Ali
 * 
 * @param <T> is generic type of Entity 
 */
class FieldReader {
	/**
	 * Finds non-annotated field from ViewModel and maps it to its corresponding value from the Entity
	 * @param modelField A field from ViewModel class
	 * @param fieldsFromEntity Declared fields in Entity class
	 * @param entity An object of Type {@link T}
	 * @param <T> is generic type of Entity 
	 * @return An object of type {@link se.codepool.automapper.utilities.FieldEntry FieldEntry}
	 */
	static <T> FieldEntry findNonAnnotatedField(Field modelField, List<Field> fieldsFromEntity, T entity) {
		Optional<Field> result = fieldsFromEntity.parallelStream()
												.filter(m -> 
														isSameReturnType(m, modelField) && 
														m.getName().equals(modelField.getName())
														)
												.findFirst();
		if (result.isPresent()) {
			try {
				Field field = result.get();
				field.setAccessible(true);
				return new FieldEntry(modelField, field.get(entity));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		throw new FieldNotFoundException("No match found for '" + modelField.getName() + "'");
	}
	/**
	 * Finds annotated field from ViewModel and maps it to its corresponding value from the Entity
	 * @param modelField A field from ViewModel class
	 * @param fieldsFromEntity Declared fields in Entity class
	 * @param entity An object of Type {@link T}
	 * @param <T> is generic type of Entity 
	 * @return An object of type {@link se.codepool.automapper.utilities.FieldEntry FieldEntry}
	 */
	static <T> FieldEntry findAnnotatedField(Field modelField, List<Field> fieldsFromEntity, T entity) {
		if (!isAnnotated(modelField))
			throw new InvalidAnnotationException("MapTo or NonCircularReference annotation is required "
					+ "for field " + modelField.getName() + " in " + modelField.getDeclaringClass().getName());
		
		MapTo an = modelField.getAnnotation(MapTo.class);
		if (an == null) {
			NonCircularReference cr = modelField.getAnnotation(NonCircularReference.class);
			if(cr != null){
				if(!isCircularReferenceSolvable(modelField))
					throw new InvalidAnnotationException(modelField.getName() + " has invalid type."
							+ "Either use correct type or replace the annotation with MapTo");
				Optional<Field> result = fieldsFromEntity.parallelStream()
												.filter(x -> 
														x.getType().equals(modelField.getType())
														)
												.findFirst();
				if(result.isPresent()) {
					try {
						Field field = result.get();
						Class<?> declaredClass = field.getDeclaringClass();
						field.setAccessible(true);
						Object value = field.get(entity);
						// check first if it is a single object or a list
						Field[] fields;
						Field circularField;
						if(isCollection(field.getType()) || isMap(field.getType()) || field.getType().isArray()) {
							fields = getFieldsFromGenericCollection(field);
							circularField = getCircularField(declaredClass, fields);
							if(field.getType().isArray()) {
								Object[] objs = (Object[]) value;
								circularField.setAccessible(true);
								for(Object o : objs)
									circularField.set(o, null);
							} else {
								Collection<Object> objs = (Collection<Object>) value;
								circularField.setAccessible(true);
								for(Object o : objs)
									circularField.set(o, null);
							}
						} else {
							fields = value.getClass().getDeclaredFields();
							circularField = getCircularField(declaredClass, fields);
							circularField.setAccessible(true);
							circularField.set(value, null);
						}
						return new FieldEntry(modelField, field.get(entity));
					} catch (IllegalArgumentException | IllegalAccessException | SecurityException | ClassNotFoundException e) {
						throw new RuntimeException(e.getMessage());
					}
				}
				throw new InvalidAnnotationException(
						modelField.getName() + " has no match in the given Entity.");
			}
			MapFrom mapFrom = modelField.getAnnotation(MapFrom.class);
			if(mapFrom != null) {
				String[] getterMethodNames = mapFrom.getterMethods();
				Method getterMethod = null;
				ValueBuilder val = null;
				for(String g : getterMethodNames) {
					try {
						getterMethod = entity.getClass().getDeclaredMethod(g);
						if(val == null)
							val = new ValueBuilder(getterMethod.invoke(entity));
						else
							val.updateValue(getterMethod.invoke(entity));
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						throw new RuntimeException(e1.getMessage());
					}
				}
				return new FieldEntry(modelField, val.getValue());
			}
		}
		String getterMethodName = an.getterMethod();
		Method getterMethod = null;
		try {
			getterMethod = entity.getClass().getDeclaredMethod(getterMethodName);
		} catch (NoSuchMethodException | SecurityException e1) {
			throw new RuntimeException(e1.getMessage());
		}
		if(getterMethod != null) {
			String[] selectedProperties = an.selectedProperties();
			ValueBuilder val = null;
			for(String g : selectedProperties) {
				try {
					Object getterObject = getterMethod.invoke(entity);
					Method getter = getterMethod.getReturnType().getDeclaredMethod(g);
					if(selectedProperties.length > 1) {
						if(!getter.getReturnType().isPrimitive() && getter.getReturnType() != String.class)
							throw new InvalidArgumentOnAnnotationException("MapTo selectedProperties "
									+ "must be primitive in case more than one getter is selected.");
					}
					if(val == null)
						val = new ValueBuilder(getter.invoke(getterObject));
					else
						val.updateValue(getter.invoke(getterObject));
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
			return new FieldEntry(modelField, val.getValue());
		}
		throw new InvalidAnnotationException("The getter method '" +
				getterMethodName + "' provided in the annotation has no match in the given Entity.");
		
	}

	/**
	 * Checks if given field is annotated with either MapTo or NonCircularReference
	 * annotation, but not both.
	 * 
	 * @param field
	 *            Given field from ViewModel to check
	 * @return Returns true if MapTo or NonCircularReference is presented, otherwise
	 *         false
	 */
	static boolean isAnnotated(Field field) {
		return (field.isAnnotationPresent(MapTo.class) 
			   ^ 
			   field.isAnnotationPresent(NonCircularReference.class))
			   ^
			   field.isAnnotationPresent(MapFrom.class);
	}
	
	/**
	 * Finds circular reference field
	 * @param declaredClass Entity type to be found in the reference object
	 * @param fields all filed in the Entity
	 * @return a field that is a reference back to the Entity
	 */

	private static Field getCircularField(Class<?> declaredClass, Field[] fields) {
		Optional<Field> result = Arrays.stream(fields)
										.filter(x -> x.getType().equals(declaredClass))
										.findFirst();
		if(result.isPresent())
			return result.get();
		throw new FieldNotFoundException(declaredClass.getName() +  " not found");
	}
	
	/**
	 * Checks if two fields has the same return type
	 * @param firstField first field to check
	 * @param secondField second field to be compared with
	 * @return true if both field have the same return type, otherwise false
	 */
	private static boolean isSameReturnType(Field firstField, Field secondField) {
		return firstField.getType().equals(secondField.getType());
	}
	
	/**
	 * Checks if given field is user defined type, in that case it's a circular reference
	 * @param field the field to be checked
	 * @return true if it's user defined type, otherwise false
	 */
	private static boolean isCircularReferenceSolvable(Field field) {
		if (field.getType().isPrimitive())
			return false;
		if (isCollection(field.getType()) || isMap(field.getType())) {
			Optional<Type> genericType = getGenericTypeFromCollection(field);
			return genericType.isPresent();
		} else if(field.getType().isArray()) {
			Class<?> componentType = getGenericTypeFromArray(field);
			return !componentType.isPrimitive() &&
				   !ClassVendor.isJDKDefinedType(componentType.getName());
		}
		return !ClassVendor.isJDKDefinedType(field.getType().getName());
	}
	/**
	 * Returns the generic type from a field of type array
	 * @param field given field to get its generic type
	 * @return Generic type which used in the given field
	 */
	private static Class<?> getGenericTypeFromArray(Field field) {
		return field.getType().getComponentType();
	}
	
	/**
	 * Gets all declared fields in given field
	 * @param field field to be read
	 * @return	an array of fields
	 */
	private static Field[] getFieldsFromGenericCollection(Field field) throws SecurityException, ClassNotFoundException {
		if(isCollection(field.getType()) || isMap(field.getType())) {
			Optional<Type> genericType = getGenericTypeFromCollection(field);
			return Class.forName(genericType.get().getTypeName()).getDeclaredFields();
		} else {
			Class<?> componentType = getGenericTypeFromArray(field);
			return componentType.getDeclaredFields();
		}
	}
	
	/**
	 * Returns the generic type from a field of type collection
	 * @param field given field to get its generic type
	 * @return Generic type which used in the given field
	 */
	private static Optional<Type> getGenericTypeFromCollection(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		Type[] types = pt.getActualTypeArguments();
		Optional<Type> result = Arrays.asList(types)
										.stream()
										.filter(x -> 
												!x.getClass().isPrimitive() && 
												!ClassVendor.isJDKDefinedType(x.getTypeName())
												)
										.findFirst();
		return result;
	}
	/**
	 * Checks if given type is a Collection type
	 * @param type given type to be checked
	 * @return true if it's a collection, otherwise false.
	 */
	private static boolean isCollection(Class<?> type) {
		return Collection.class.isAssignableFrom(type);
	}
	/**
	 * Checks if given type is a Map type
	 * @param type given type to be checked
	 * @return true if it's a Map, otherwise false.
	 */
	private static boolean isMap(Class<?> type) {
		return Map.class.isAssignableFrom(type);
	}
}
