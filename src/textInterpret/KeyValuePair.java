package textInterpret;

import java.util.Map;

public class KeyValuePair implements Map.Entry<Object, Object>
{
	private final Object key;
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
