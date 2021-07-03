package textInterpret;

import java.util.Map;

/**
 * Concrete class implementing {@link java.util.Map.Entry Map.Entry}<code>&lt;Object, Object&gt;</code>
 * 
 * @author kieran
 */
public class KeyValuePair implements Map.Entry<Object, Object>
{
	/**
	 * Stores the pair's key
	 */
	private final Object key;
	
	/**
	 * Stores the pair's value
	 */
	private Object value;
	
	public KeyValuePair(Object key, Object value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public Object getKey()
	{
		return key;
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public Object setValue(Object value)
	{
		Object oldVal = this.value;
		this.value = value;
		return oldVal;
	}
	
	@Override
	public String toString()
	{
		return "[" + key.toString() + ", " + value.toString() + "]";
	}
}
