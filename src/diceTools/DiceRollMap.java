package diceTools;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementation of {@link ProbMap} with <code>Integer</code> values used for event keys.
 * 
 * <p>Event keys are never permitted to be <code>null</code>.
 * 
 * @author kieran
 */
public class DiceRollMap extends ProbMap<Integer>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums together two <code>Integer</code>s
	 */
	private static final BiFunction<Integer, Integer, Integer> sumCombiner;
	
	/**
	 * Sums each <code>Integer</code> in a <code>List&ltInteger&gt</code> key
	 */
	private static final Function<List<Integer>, Integer> sumFlattener;
	
	static
	{
		sumCombiner = new BiFunction<Integer, Integer, Integer>()
		{
			@Override
			public Integer apply(Integer a, Integer b)
			{
				return a + b;
			}
		};
		
		sumFlattener = new Function<List<Integer>, Integer>()
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
	}
	
	public DiceRollMap()
	{
		super();
	}
	
	public DiceRollMap(DiceRollMap drm)
	{
		super(drm);
	}
	
	public DiceRollMap(int initialCapacity)
	{
		super(initialCapacity);
	}
	
	public DiceRollMap(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}
	
	@Override
	public boolean keyIsValid(Integer key) throws InvalidKeyException
	{
		return key != null;
	}
	
	@Override
	public Integer sanitizeKey(Integer key)
	{
		return key;
	}
	
	@Override
	public DiceRollMap get()
	{
		return new DiceRollMap();
	}
	
	/**
	 * Generate the {@link DiceRollMap} which results from rolling a set of identical unbiased dice
	 * @param numDice	number of dice to be rolled
	 * @param sides		number of sides on the dice
	 * @return			probabilities of all possible outcomes
	 */
	public static DiceRollMap diceRoll(int numDice, int sides)
	{
		DiceRollMap drm = new DiceRollMap();
		
		DiceRollIterable dri = new DiceRollIterable(numDice, sides);
		for (Entry<List<Integer>, Double> entry : dri)
		{
			Integer key = sumFlattener.apply(entry.getKey());
			drm.merge(key, entry.getValue());
		}
		
		return drm;
	}
	
	/**
	 * Generate all the possible results of summing the roll outcomes
	 * of the calling {@link DiceRollMap} with the given <code>DiceRollMap</code>.
	 * @param drm	<code>DiceRollMap</code> to combine
	 * @return		resulting <code>DiceRollMap</code>
	 */
	public DiceRollMap combine(DiceRollMap drm)
	{
		if (this.isEmpty())
		{
			DiceRollMap drmNew = new DiceRollMap();
			drmNew.putAll(drm);
			return drmNew;
		}
		
		return (DiceRollMap) combine(sumCombiner, drm);
	}
}
