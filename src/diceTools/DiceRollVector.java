package diceTools;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementation of {@link ProbMap} with <code>List&ltInteger&gt</code> values used for event keys.
 * 
 * <p>Event keys are never permitted to be <code>null</code>,
 * nor are they permitted to contain <code>null</code> values.
 * The ordering of <code>List</code> keys is not preserved,
 * as they are sorted before accessing the underlying <code>HashMap</code>.
 * 
 * @author kieran
 */
public class DiceRollVector extends ProbMap<List<Integer>>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums each <code>Integer</code> in a <code>List&ltInteger&gt</code> key
	 */
	private static final Function<List<Integer>, Integer> sumFlatten;
	
	/**
	 * Copies a <code>List</code> of <code>Integer</code>s
	 * and <code>add</code>s an <code>Integer</code> to the copy
	 */
	private static final BiFunction<List<Integer>, Integer, List<Integer>> listAdd;
	
	/**
	 * Copies a <code>List</code> of <code>Integer</code>s
	 * and <code>add</code>s all <code>Integer</code>s
	 * from another <code>List</code> to the copy.
	 */
	private static final BiFunction<List<Integer>, List<Integer>, List<Integer>> listAddAll;
	
	static
	{
		sumFlatten = new Function<List<Integer>, Integer>()
		{
			@Override
			public Integer apply(List<Integer> key)
			{
				int total = 0;
				
				for (Integer myInt : key)
					total += myInt;
				
				return total;
			}
		};
		
		listAdd = new BiFunction<List<Integer>, Integer, List<Integer>>()
		{
			@Override
			public List<Integer> apply(List<Integer> t, Integer u)
			{
				List<Integer> newList = new LinkedList<Integer>(t);
				newList.add(u);
				return newList;
			}
		};
		
		listAddAll = new BiFunction<List<Integer>, List<Integer>, List<Integer>>()
		{
			@Override
			public List<Integer> apply(List<Integer> t, List<Integer> u)
			{
				List<Integer> newList = new LinkedList<Integer>(t);
				newList.addAll(u);
				return newList;
			}
		};
	}
	
	@Override
	public boolean keyIsValid(List<Integer> key)
	{
		if (key == null)
			return false;
		
		for (Integer keyInt : key)
			if (keyInt == null)
				return false;
		
		return true;
	}
	
	@Override
	public List<Integer> sanitizeKey(List<Integer> key)
	{
		List<Integer> sanitizedKey = new LinkedList<Integer>(key);
		Collections.sort(sanitizedKey);
		sanitizedKey = new ImmutableList<Integer>(sanitizedKey);
		return sanitizedKey;
	}
	
	@Override
	public List<Integer> makeKey(Object oKey)
	{
		System.out.printf("oKey: %s\n", oKey.toString());
		
		if ((oKey instanceof Integer[]) || (oKey instanceof int[]))
		{
			Integer[] keyArr = (Integer[]) oKey;
			return Arrays.asList(keyArr);
		}
		
		@SuppressWarnings("unchecked")
		List<Integer> key = (List<Integer>) oKey;
		return key;
	}
	
	@Override
	public DiceRollVector get()
	{
		return new DiceRollVector();
	}
	
	/**
	 * Generate the {@link DiceRollVector} which results from rolling a set of identical unbiased dice 
	 * @param numDice	number of dice to be rolled
	 * @param sides		number of sides on the dice
	 * @return			probabilities of all possible outcomes
	 */
	public static DiceRollVector diceRoll(int numDice, int sides)
	{
		DiceRollIterable dri = new DiceRollIterable(numDice, sides);
		
		DiceRollVector drv = new DiceRollVector();
		drv.putAll(dri);

		return drv;
	}
	
	/**
	 * Generate all the possible results of combining each roll array from the calling
	 * {@link DiceRollVector} with each possible roll from the given {@link ProbVector}.
	 * @param pv	<code>ProbVector</code> to be combined
	 * @return		resulting <code>DiceRollVector</code>
	 */
	public DiceRollVector combine(ProbVector pv)
	{
		if (this.isEmpty())
		{
			DiceRollVector drvNew = new DiceRollVector();
			
			for (Entry<Integer, Double> entry : pv.entrySet())
			{
				List<Integer> listNew = new LinkedList<Integer>();
				listNew.add(entry.getKey());
				drvNew.put(listNew, entry.getValue());
			}
			
			return drvNew;
		}
		
		return (DiceRollVector) combine(listAdd, pv, this);
	}
	
	/**
	 * Generate all the possible results of combining each roll array from the calling
	 * {@link DiceRollVector} with each roll array from the given <code>DiceRollVector</code>.
	 * @param drv	<code>DiceRollVector</code> to be combined
	 * @return		resulting <code>DiceRollVector</code>
	 */
	public DiceRollVector combine(DiceRollVector drv)
	{
		if (this.isEmpty())
		{
			DiceRollVector drvNew = new DiceRollVector();
			drvNew.putAll(drv);
			return drvNew;
		}
		
		return (DiceRollVector) combine(listAddAll, drv, this);
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @param function	function which combines the rolls
	 * @return			{@link ProbVector} storing the combined rolls and their probabilities
	 */
	public ProbVector flatten(Function<List<Integer>, Integer> function)
	{
		return (ProbVector) super.morph(function, new ProbVector().supplyMe());
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @return			{@link ProbVector} storing the summed rolls and their probabilities
	 */
	public ProbVector flatten()
	{
		return flatten(sumFlatten);
	}
}
