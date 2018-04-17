package se.codepool.automapper.utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class used to receive the desired Entity as either a single object or a collection and converts it to 
 * the provided ViewModel type. It clones the data from {@link TYPE} and creates new object of {@link RESULT}
 * @author Ali
 *
 * @param <TYPE> Represents Entity type
 * @param <RESULT> Represents ViewModel type
 */

class ClassMapper<TYPE, RESULT> {
	
	private List<RESULT> models = new ArrayList<>();
	private Class<RESULT> model;
	private TYPE entity;
	private List<TYPE> entities;
	/**
	 * Constructor to set the {@link RESULT} which is the ViewModel as well as {@link TYPE} which is a collection of type Entity
	 * @param entities Represents the given Entity
	 * @param model Represents the given ViewModel
	 */
	ClassMapper(List<TYPE> entities, Class<RESULT> model) {
		this.model = model;
		this.setEntities(entities);
	}
	/**
	 * Constructor to set the {@link RESULT} which is the ViewModel as well as {@link TYPE} which is an Entity
	 * @param model
	 * @param entity
	 */
	ClassMapper(Class<RESULT> model, TYPE entity) {
		this.entity = entity;
		this.model = model;
	}
	/**
	 * This method used to return the result of type {@link RESULT} after passing a list of entities of Type {@link TYPE}
	 * @return A list of {@link RESULT}
	 * @throws 
	 * 		throws a {@link NullPointerException} if it's called after passing a single object
	 */
	List<RESULT> getViewModels() {
		if(this.entities.isEmpty())
			return this.models;
		return this.mapEntities();
	}
	/**
	 * This method used to return the result of type {@link RESULT} after passing an object of Type {@link TYPE}
	 * @return An object of {@link RESULT}
	 * @throws 
	 * 		throws a {@link NullPointerException} if it's called after passing a list of {@link TYPE} 
	 * 		or passing object is null.
	 */
	RESULT getViewModel() {
		if(this.entity == null)
			throw new NullPointerException("No entity was found.");
		return mapEntity();
	}
	/**
	 * Creates a new instance of ViewModel and assigns values from given Entity to the ViewModel
	 * @return A ViewModel object of type {@link RESULT}
	 */
	private RESULT mapEntity() {
		RESULT tempInstance = ClassReader.getNewInstance(this.model);
		Map<Field, Object> nonAnnotated = ClassReader.mapNonAnnotatedFields(this.model, this.entity);
		Map<Field, Object> annotated = ClassReader.mapAnnotatedFields(this.model, this.entity);
		for(Map.Entry<Field, Object> field : nonAnnotated.entrySet()) {
			field.getKey().setAccessible(true);
			try {
				field.getKey().set(tempInstance, field.getValue());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		for(Map.Entry<Field, Object> field : annotated.entrySet()) {
			field.getKey().setAccessible(true);
			try {
				field.getKey().set(tempInstance, field.getValue());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return tempInstance;
	}
	/**
	 * Iterates through all entities and passes them to {@link #mapEntity() mapEntity} function
	 * @return A list of type {@link RESULT}
	 */
	private List<RESULT> mapEntities() {
		RESULT tempInstance;
		for(TYPE entity : this.entities) {
			this.entity = entity;
			tempInstance = this.mapEntity();
			this.models.add(tempInstance);
		}
		return this.models;
	}
	/**
	 * Sets {@link #entities entities} from the constructor
	 * @param entities A list of type {@link TYPE}
	 */
	private void setEntities(List<TYPE> entities) {
		if(entities == null)
			throw new NullPointerException("Passed object is null.");
		this.entities = entities;
	}
}
