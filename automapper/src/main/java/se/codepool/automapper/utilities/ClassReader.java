package se.codepool.automapper.utilities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * A class used to read classes, map annotated fields and non-annotated fields and create instance of the ViewModel
 * @author Ali
 * 
 * @param <T> is generic type of Entity 
 * @param <R> is generic type of ViewModel 
 */
class ClassReader {
	/**
	 * Creates a new instance of the ViewModel
	 * @param clazz given type to create a new instance of
	 * @return a new instance of type {@link R}
	 * @throws throws {@link RuntimeException RuntimeException} if this Class represents an abstract 
	 *  class, an interface, an array class, a primitive type, or void; or if the class has no nullary 
	 *  constructor; or if the instantiation fails for some other reason. Or if the class or its nullary constructor is not accessible.
	 */
	static <R> R getNewInstance(Class<R> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * Maps all values from given Entity to its corresponding non-annotated fields in the given ViewModel
	 * @param model given ViewModel type
	 * @param entity given Entity type
	 * @return A Map that has a field from ViewModel as a Key and value from Entity as a value
	 */
	static <T, R> Map<Field, Object> mapNonAnnotatedFields(Class<R> model, T entity) {
		Class<?> entityType = entity.getClass();
		List<Field> getters = getFields(entityType);
		List<Field> setters = getFields(model);
		 return setters.stream()
				 		.filter(x -> !FieldReader.isAnnotated(x))
						.map(x -> FieldReader.findNonAnnotatedField(x, getters, entity))
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
	/**
	 * Maps all values from given Entity to its corresponding annotated fields in the given ViewModel
	 * @param model given ViewModel type
	 * @param entity given Entity type
	 * @return A Map that has a field from ViewModel as a Key and value from Entity as a value
	 */
	static <T, R> Map<Field, Object> mapAnnotatedFields(Class<R> model, T entity) {
		Class<?> entityType = entity.getClass();
		List<Field> getters = getFields(entityType);
		List<Field> setters = getFields(model);
		return setters.stream()
				 		.filter(x -> FieldReader.isAnnotated(x))
						.map(x -> FieldReader.findAnnotatedField(x, getters, entity))
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
	
	/**
	 * Gets all declared fields from given type
	 * @param clazz a type to get its fields
	 * @return a list of fields
	 */
	private static List<Field> getFields(Class<?> clazz) {
		return Arrays.asList(clazz.getDeclaredFields());
	}
}
