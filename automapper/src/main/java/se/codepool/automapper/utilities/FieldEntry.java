package se.codepool.automapper.utilities;

import java.lang.reflect.Field;
import java.util.Map.Entry;

/**
 * A class implements Map.Entry to store the field from the ViewModel as a Key and the value from the Entity as a Value 
 * @author Ali
 */
class FieldEntry implements Entry<Field, Object> {
    private final Field key;
    private Object value;

    FieldEntry(Field key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Field getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
    	Object old = this.value;
        this.value = value;
        return old;
    }
}
