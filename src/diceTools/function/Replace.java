package diceTools.function;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * {@link java.util.function.Function#Function Function} to replace some
 * values in a list with others. This process is performed by copying the
 * <code>List</code>, then performing the replacement on the copy,
 * which is returned.
 *
 * <p><code>Replace&ltInteger&gt</code> is a valid argument for
 * {@link diceTools.DiceRollVector#morph(Function) DiceRollVector.morph(Function)}
 *
 * <T> type of the elements in the <code>List</code>
 *
 * @author kieran
 **/
public class Replace <T> implements Function<List<T>, List<T>>
{
	//TODO: do this with Function<? super T, ? extends T> instead of a HashMap
	
	/**
	 * Mapping of each element to be considered (key)
	 * to the element which replaces it (value)
	 **/
	private final Map<T, T> replaceMap;
	
	/**
	 * Constructs a <code>Replace</code> which replaces elements in a
	 * <code>List</code> which are equal to a given value with
	 * another value
	 * @param oldVal	value to be replaced
	 * @param newVal	value which replaces it
	 **/
	public Replace(T oldVal, T newVal)
	{
		replaceMap = new HashMap<T, T>(2);
		replaceMap.put(oldVal, newVal);
	}
	
	/**
	 * Constructs a <code>Replace</code> which replaces elements in a
	 * <code>List</code> which are equal to keys in a given
	 * <code>Map</code> with those keys' corresponding values.
	 * Any element in a <code>List</code> which has no key in the
	 * <code>Map</code> is simply copied to the new <code>List</code>
	 * @param replaceMap	<code>Map</code> giving the mappings
							from keys to be replaced, to the values
							which replace them
	 **/
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
