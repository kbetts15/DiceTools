package diceTools.function;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@link java.util.function.Function#Function Function}
 * to count <code>Integer</code>'s in a <code>List</code>.
 * Counting is performed by checking each <code>Integer</code> with a
 * {@link java.util.function.Predicate#Predicate Predicate}, and incrementing
 * the count iff it returns true.
 * 
 * <p><code>Count</code> is a valid argument for
 * {@link diceTools.DicePoolMap#flatten(Function) DicePoolMap.flatten(Function)}
 * 
 * @author kieran
 */
public class Count implements Function<List<Integer>, Integer>
{
	//TODO: reimplement Count to act on DiceNumbers rather than Integers
	
	/**
	 * {@link java.util.function.Predicate#Predicate Predicate}
	 * used to determine which <code>Integer</code>'s to count
	 */
	private final Predicate<Integer> pred;
	
	/**
	 * Constructs a <code>Count</code> with a given
	 * {@link java.util.function.Predicate#Predicate Predicate}.
	 * @param pred <code>Predicate</code> to determine which <code>Integer</code>s should be counted
	 */
	public Count(Predicate<Integer> pred)
	{
		if (pred == null)
			this.pred = n -> true;
		else
			this.pred = pred;
	}
	
	@Override
	public Integer apply(List<Integer> li)
	{
		if (li == null)
			throw new NullPointerException("Input List is null");
		
		int count = 0;
		
		for (Integer checkInt : li)
			if (pred.test(checkInt))
				count++;
		
		return count;
	}

	/**
	 * Generates a <code>Count</code> which counts the number of <code>Integer</code>s
	 * in a <code>List</code> which are equal to some value.
	 * @param val	value to check for
	 * @return		generated <code>Count</code>. Note that the <code>Count</code> does not handle
	 * 				<code>null</code> values in the <code>List</code>
	 */
	public static Count countVal(int val)
	{
		Predicate<Integer> p = (x) -> {return x.equals(val);};
		return new Count(p);
	}
}
