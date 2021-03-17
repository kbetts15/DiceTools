package diceTools.function;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Replace <T> implements Function<List<T>, List<T>>
{
	private final Map<T, T> replaceMap;
	
	public Replace(T oldVal, T newVal)
	{
		replaceMap = new HashMap<T, T>(2);
		replaceMap.put(oldVal, newVal);
	}
	
	public Replace(Map<? extends T, ? extends T> replaceMap)
	{
		this.replaceMap = new HashMap<T, T>(replaceMap);
	}
	
	@Override
	public List<T> apply(List<T> li)
	{
		List<T> newList = new LinkedList<T>();
		
		for (T t : li)
		{
			if (replaceMap.containsKey(t))
				newList.add(replaceMap.get(t));
			else
				newList.add(t);
		}
		
		return newList;
	}

}
