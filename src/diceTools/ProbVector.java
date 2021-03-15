package diceTools;

import java.util.function.BiFunction;

/**
 * Implementation of {@link ProbMap} with <code>Integer</code> values used for event keys.
 * 
 * <p>Event keys are never permitted to be <code>null</code>.
 * 
 * @author kieran
 */
public class ProbVector extends ProbMap<Integer>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums together two <code>Integer</code>s
	 */
	private static final BiFunction<Integer, Integer, Integer> sumCombiner;
	
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
	public ProbVector get()
	{
		return new ProbVector();
	}
	
	//TODO: generate diceRoll using binomial maths
	/**
	 * Generate the {@link ProbVector} which results from rolling a set of identical unbiased dice
	 * @param numDice	number of dice to be rolled
	 * @param sides		number of sides on the dice
	 * @return			probabilities of all possible outcomes
	 */
	public static ProbVector diceRoll(int numDice, int sides)
	{
		//TODO: do this withour involving DiceRollVector
		
		return DiceRollVector.diceRoll(numDice, sides).flatten();
	}
	
	/**
	 * Generate all the possible results of summing the roll outcomes
	 * of the calling {@link ProbVector} with the given <code>ProbVector</code>.
	 * @param pv	<code>ProbVector</code> to combine
	 * @return		resulting <code>ProbVector</code>
	 */
	public ProbVector combine(ProbVector pv)
	{
		if (this.isEmpty())
		{
			ProbVector pvNew = new ProbVector();
			pvNew.putAll(pv);
			return pvNew;
		}
		
		return (ProbVector) combine(sumCombiner, pv);
	}
}
