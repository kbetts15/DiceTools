package diceTools.function;

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
 * {@link diceTools.DicePoolMap#morph(Function) DicePoolMap.morph(Function)}
 *
 * <T> type of the elements in the <code>List</code>
 *
 * @author kieran
 **/
public class Replace <T> implements Function<List<T>, List<T>>
{
	/**
	 * <code>Function</code> defining the mapping between elements
	 * to be replaced, and their replacements 
	 */
	private final Function<T, T> replaceFunc;
	
	/**
	 * Constructs a <code>Replace</code> which replaces elements in a
	 * <code>List</code> which are equal to a given value with
	 * another value
	 * @param oldVal	value to be replaced
	 * @param newVal	value which replaces it
	 **/
	public Replace(T oldVal, T newVal)
	{
		replaceFunc = (x) -> {return x.equals(oldVal) ? newVal : null;};
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
	public Replace(Map<? super T, ? extends T> replaceMap)
	{
		replaceFunc = (x) -> {return replaceMap.get(x);};
	}
	
	/**
	 * Constructs a <code>Replace</code> which replaces elements in a
	 * <code>List</code> according to the mapping defined by a
	 * {@link java.util.function.Function#Function Function}.
	 * When <code>Function.apply</code> returns null, the element is not replaced.
	 * @param replaceFunc	<code>Function</code> defining the mappings from
	 * 						elements to be replaced, to what they are replaced with
	 */
	public Replace(Function<T, T> replaceFunc)
	{
		this.replaceFunc = replaceFunc;
	}
	
	@Override
	public List<T> apply(List<T> li)
	{
		List<T> newList = new LinkedList<T>();
		
		for (T t : li)
		{
			T newItem = replaceFunc.apply(t);
			
			if (newItem == null)
				newList.add(t);
			else
				newList.add(newItem);
		}
		
		return newList;
	}

}
