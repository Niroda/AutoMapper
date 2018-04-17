package se.codepool.automapper.utilities;

import java.util.ArrayList;
import java.util.List;
import se.codepool.automapper.annotations.ViewModel;
import se.codepool.automapper.exceptions.InvalidEntityAnnotationException;
/**
 * The main class for automapper. It takes the desired ViewModel and either an object or list of object of type Entity
 * @author Ali
 * 
 * @param <T> Represents the type of the Entity
 * @param <R> Represents the ViewModel type.
 */
public final class Mapper<R> {

	private Class<R> model;
	private Class<?> declaredTypeInAnnotation;
	
	/**
	 * A constructor to set the ViewModel and to get the Entity type from the annotation.
	 * @param model Represents the desired ViewModel
	 * @param vm Represents the ViewModel-Annotation, it's used to read the Entity class.
	 */
	private Mapper(Class<R> model, ViewModel vm) {
		this.model = model;
		this.declaredTypeInAnnotation = vm.entity();
	}
	
	/**
	 * A static method used to set the desired ViewModel and pass that model to the constructor.
	 * @param model Represents the ViewModel type.
	 * @return a new instance of Mapper.
	 * @throws throws {@link IllegalArgumentException IllegalArgumentException} if the model is null or of primitive 
	 * type or if the {@link se.codepool.automapper.annotations.ViewModel ViewModel} annotation is not set
	 */
	public static <R> Mapper<R> setViewModel(Class<R> model) {
		if(model == null || model.isPrimitive())
			throw new IllegalArgumentException(model.getName() + " neither can be null nor primitive.");
		ViewModel vm = model.getAnnotation(ViewModel.class);
		if(vm == null)
			throw new IllegalArgumentException("Add ViewModel annotation to " + model.getName());
		return new Mapper<R>(model, vm);
	}
	
	/**
	 * Converts given Entity to Viewmodel
	 * @param <T> Represents the type of the Entity
	 * @param entity given object of type {@link T} to be converted
	 * @return an object of type {@link R}
	 */
	public <T> R map(T entity) {
		this.checkProvidedTypeInAnnotation(entity);
		return new ClassMapper<>(this.model, entity).getViewModel();
	}
	/**
	 * Converts given list of Entity to a list of Viewmodel
	 * @param <T> Represents the type of the Entity
	 * @param entities given list of type {@link T} to be converted
	 * @return a list of type {@link R}
	 */
	public <T> List<R> map(List<T> entities) {
		if(entities.size() > 0)
			this.checkProvidedTypeInAnnotation(entities.get(0));
		else
			return new ArrayList<>();
		return new ClassMapper<>(entities, this.model).getViewModels();
	}
	/**
	 * Checks if the provided Entity type matches the declared type in Viewmodel annotation
	 * @param entity given Entity of type {@link T} to be checked
	 */
	private <T> void checkProvidedTypeInAnnotation(T entity) {
		Class<?> type = entity.getClass();
		if(type != this.declaredTypeInAnnotation)
			throw new InvalidEntityAnnotationException("Given Entity '" + entity.getClass() + "'"
					+ " doesn't match declared Entity in the ViewModel annotation.");
	}
}
