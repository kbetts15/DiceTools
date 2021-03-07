import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
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
public class ProbVector extends HashMap<Integer, Double>
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public Double put(Integer key, Double value)
	{
		if (key == null || value == null)
			return null;
		
		return super.put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Double> map)
	{
		map.forEach(putter);
	}
	
	@Override
	public Double putIfAbsent(Integer key, Double value)
	{
		if (key == null || value == null)
			return null;
		
		return super.putIfAbsent(key, value);
	}
	
	@Override
	public Double replace(Integer key, Double value)
	{
		if (key == null || value == null)
			return null;
		
		return super.replace(key, value);
	}
	
	@Override
	public boolean replace(Integer key, Double oldValue, Double newValue)
	{
		if (key == null || newValue == null)
			return false;
		
		return super.replace(key, oldValue, newValue);
	}
	
	@Override
	public void replaceAll(BiFunction<? super Integer, ? super Double, ? extends Double> function)
	{
		super.replaceAll(function);
		super.forEach(removeNullValues);
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append('{');
		
		for (Integer myInt : this.keySet())
		{
			if (s.length() != 1)
				s.append(", ");
				
			s.append(myInt);
			s.append(": ");
			s.append(String.format("%5.3f", this.get(myInt)));
		}
		
		s.append('}');
		
		return s.toString();
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
	
	/**
	 * Get whether the stored probabilities sum to 1.0
	 * @return true if the stored probabilities sum to 1.0, false otherwise
	 */
	public boolean hasValidProb()
	{
		double total = 0;
		for (Double prob : this.values())
			total += prob;
		return total == 1.0;
	}
	
	private BiConsumer<Integer, Double> putter = new BiConsumer<Integer, Double>()
			{
				@Override
				public void accept(Integer key, Double value)
				{
					put(key, value);
				}
			};
	
	private BiConsumer<Integer, Double> removeNullValues = new BiConsumer<Integer, Double>()
			{
				@Override
				public void accept(Integer key, Double value)
				{
					if (value == null)
						remove(key);
				}
			};
			
	private static final BiFunction<Integer, Integer, Integer> sumCombine = new BiFunction<Integer, Integer, Integer>()
			{
				@Override
				public Integer apply(Integer a, Integer b)
				{
					return a + b;
				}
			};
}
