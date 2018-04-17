package se.codepool.automapper.utilities;

import se.codepool.automapper.exceptions.InvalidTypeException;

class ValueBuilder {
	
	private Object value;
	ValueBuilder(Object initValue) {
		this.value = initValue;
	}
	
	Object getValue() {
		return this.value;
	}
	
	void updateValue(Object value) {
		this.value = this.getValueType(value);
	}
	
	private Object getValueType(Object value) {
		Class<?> type = value.getClass();
		if(type == Integer.class)
			return new Integer((Integer)this.value + (Integer)value);
		else if(type == Float.class)
			return new Float((Float)this.value + (Float)value);
		else if(type == Double.class)
			return new Double((Double)this.value + (Double)value);
		else if(type == Long.class)
			return new Long((Long)this.value + (Long)value);
		else if(type == Short.class) {
			short result = (short)((short)this.value + (short)value);
			return new Short(result);
		} else if(type == String.class)
			return new String((String)this.value + " " + (String)value);
		else
			throw new InvalidTypeException(type.getName() + " is not supported.");
	}
	
}
