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
public class DiceRollMap extends ProbMap<DiceNumber>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums together two <code>Integer</code>s
	 */
	private static final BiFunction<DiceNumber, DiceNumber, DiceNumber> sumCombiner;
	
	/**
	 * Sums each <code>Integer</code> in a <code>List&ltInteger&gt</code> key
	 */
	private static final Function<List<? extends DiceNumber>, DiceNumber> sumFlattener;
	
	static
	{
		sumCombiner = new BiFunction<DiceNumber, DiceNumber, DiceNumber>()
		{
			@Override
			public DiceNumber apply(DiceNumber a, DiceNumber b)
			{
				if (a.isInt() && b.isInt())
					return new DiceNumber.DiceInteger(a.intValue() + b.intValue());
				else
					return new DiceNumber.DiceDouble(a.doubleValue() + b.doubleValue());
			}
		};
		
		sumFlattener = new Function<List<? extends DiceNumber>, DiceNumber>()
		{
			
			@Override
			public DiceNumber apply(List<? extends DiceNumber> key)
			{
				boolean doubleFound = false;
				int intSum = 0;
				double doubleSum = 0;
				
				for (DiceNumber myNum : key)
				{
					if (!doubleFound && !myNum.isInt())
					{
						doubleSum = intSum;
						doubleFound = true;
					}
					
					if (doubleFound)
						doubleSum += myNum.doubleValue();
					else
						intSum += myNum.intValue();
				}
				
				if (doubleFound)
					return new DiceNumber.DiceDouble(doubleSum);
				else
					return new DiceNumber.DiceInteger(intSum);
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
	public boolean keyIsValid(DiceNumber key) throws InvalidKeyException
	{
		return key != null;
	}
	
	@Override
	public DiceNumber sanitizeKey(DiceNumber key)
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
		for (Entry<List<DiceNumber.DiceInteger>, Double> entry : dri)
		{
			DiceNumber key = sumFlattener.apply(entry.getKey());
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
