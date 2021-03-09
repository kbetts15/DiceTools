import java.util.function.BiFunction;

/**
 * Stores the probabilities of numerical outcomes.
 * The data is stored as key-pair values.
 * Keys represent the numerical outcome, as an <code>Integer</code>.
 * Values represent probabilities of those outcomes, as a <code>Double</code>.
 * 
 * <p><code>ProbVector</code> can be used to store, eg:<ul>
 * <li>The result of rolling an unbiased die</li>
 * <li>The result of summing the rolls of two unbiased dice</li></ul>
 * 
 * @author kieran
 */
public class ProbVector extends ProbMap<Integer>
{
	private static final long serialVersionUID = 1L;
	
	public ProbVector()
	{
		super(new Integer(0));
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
	
	/**
	 * Generate the {@link ProbVector} which results from rolling a set of identical unbiased dice
	 * @param numDice	number of dice to be rolled
	 * @param sides		number of sides on the dice
	 * @return			probabilities of all possible outcomes
	 */
	public static ProbVector diceRoll(int numDice, int sides)
	{
		ProbVector result = new ProbVector();
		result.put(0, 1.0);
		
		if (numDice <= 0)
			return result;
		
		ProbVector die = new ProbVector();
		for (int i = 1; i <= sides; i++)
			die.put(i, 1.0 / sides);
		
		if (numDice == 1)
			return die;
		
		for (int i = 0; i < numDice; i++)
			result = result.combine(die, sumCombine);
		
		return result;
	}

	/**
	 * Generate all the possible results of combining the roll outcomes of the calling {@link ProbVector}
	 * with the given <code>ProbVector</code> using the given <code>BiFunction</code>.
	 * @param pv		<code>ProbVector</code> to combine
	 * @param function	<code>BiFunction</code> to calculate combinations
	 * @return			resulting <code>ProbVector</code>
	 */
	public ProbVector combine(ProbVector pv, BiFunction<Integer, Integer, Integer> function)
	{
		ProbVector pvNew = new ProbVector();
		
		for (Integer myInt : this.keySet())
			for (Integer pvInt : pv.keySet())
			{
				Integer newKey = function.apply(myInt, pvInt);
				
				Double newProb = pvNew.get(newKey);
				if (newProb == null)
					newProb = 0.0;
				
				newProb += this.get(myInt) * pv.get(pvInt);
				
				pvNew.put(newKey, newProb);
			}
		
		return pvNew;
	}
	
	
	//TODO: instantiate sumCombine in the static block
	private static final BiFunction<Integer, Integer, Integer> sumCombine = new BiFunction<Integer, Integer, Integer>()
			{
				@Override
				public Integer apply(Integer a, Integer b)
				{
					return a + b;
				}
			};
}
