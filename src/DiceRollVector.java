import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Stores the probabilities of sets of numerical outcomes.
 * The data is stored as key-value pairs.
 * Keys take the form of ordered <code>Integer</code> arrays, representing the sets of numerical outcomes.
 * Values are <code>Double</code>s, representing the probability of each result.
 * 
 * <p>Note that unlike a normal {@link HashMap}, no key is ever <code>null</code>,
 * nor does a key array ever contain <code>null</code>,
 * nor is a value ever <code>null</code>.
 * 
 * <p><code>DiceRollVector</code> can be used to store, eg: The results of rolling several dice.
 * 
 * @author kieran
 */
public class DiceRollVector extends ProbMap<Integer[]>
{
	//TODO: do not change Integer[] inputs by sorting them! Call .clone then sort
	//TODO: do not call Function.apply etc on Integer[] keys - clone them first
	
	private static final long serialVersionUID = 1L;
	
	public DiceRollVector()
	{
		super(new Integer[0]);
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append('{');
		
		for (Entry<Integer[], Double> entry : this.entrySet())
		{
			if (s.length() != 1)
				s.append("; ");
			
			StringBuffer ss = new StringBuffer();
			
			for (Integer myInt : entry.getKey())
			{
				if (ss.length() != 0)
					ss.append(", ");
				
				ss.append(myInt);
			}
			
			s.append(ss);
			
			s.append(": ");
			s.append(String.format("%5.3f", entry.getValue()));
		}
		
		s.append('}');
		
		return s.toString();
	}
	
	@Override
	public boolean keyIsValid(Integer[] key)
	{
		if (key == null)
			return false;
		
		for (Integer keyInt : key)
			if (keyInt == null)
				return false;
		
		return true;
	}
	
	@Override
	public Integer[] sanitizeKey(Integer[] key)
	{
		Integer[] sanitizedKey = key.clone();
		Arrays.sort(sanitizedKey);
		return sanitizedKey;
	}
	
	/**
	 * Generate the {@link DiceRollVector} which results from rolling a set of identical unbiased dice 
	 * @param numDice	number of dice to be rolled
	 * @param sides		number of sides on the dice
	 * @return			probabilities of all possible outcomes
	 */
	public static DiceRollVector diceRoll(int numDice, int sides)
	{
		DiceRollVector result = new DiceRollVector();
		
		ProbVector die = ProbVector.diceRoll(1, sides);
		
		for (int i = 0; i < numDice; i++)
			result = result.combine(die);
		
		return result;
	}
	
	/**
	 * Generate all the possible results of combining the roll arrays from the calling
	 * {@link DiceRollVector} with each possible roll from the given {@link ProbVector}
	 * @param pv	<code>ProbVector</code> to be combined
	 * @return		resulting <code>DiceRollVector</code>
	 */
	public DiceRollVector combine(ProbVector pv)
	{
		DiceRollVector drvNew = new DiceRollVector();
		
		if (this.isEmpty())
		{
			
			for (Integer pvInt : pv.keySet())
			{
				Integer[] arrNew = {pvInt};
				drvNew.put(arrNew, pv.get(pvInt));
			}
			
			return drvNew;
		}
		
//		System.out.println("Combining:");
//		System.out.printf("\t%s\n", this.toString());
//		System.out.printf("\t%s\n", pv.toString());
//		System.out.println("Results:");
		
		for (Integer[] myArr : this.keySet())
		{
			for (Integer pvInt : pv.keySet())
			{
				Integer[] newArr = Arrays.copyOf(myArr, myArr.length + 1);
				newArr[myArr.length] = pvInt;
				
				drvNew.merge(newArr, drvNew.get(newArr), sumMerger);
				System.out.printf("\t%s\n", drvNew.toString());
			}
		}
		
		return drvNew;
	}
	
	public DiceRollVector morph(Function<Integer[], Integer[]> function)
	{
		DiceRollVector drvNew = new DiceRollVector();
		
		for (Integer[] keyArr : this.keySet())
		{
			Integer[] newArr = function.apply(keyArr);
			
			drvNew.merge(newArr, this.get(keyArr), ProbVector.sumMerger);
		}
		
		return drvNew;
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @param function	function which combines the rolls
	 * @return			{@link ProbVector} storing the combined rolls and their probabilities
	 */
	public ProbVector flatten(Function<Integer[], Integer> function)
	{
		ProbVector pvNew = new ProbVector();
		
		for (Integer[] myArr : this.keySet())
		{
			Integer newInt = function.apply(myArr);
			
			Double newProb = pvNew.get(newInt);
			if (newProb == null)
				newProb = 0.0;
			
			newProb += this.get(myArr);
			
			pvNew.put(newInt, newProb);
		}
		
		return pvNew;
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @return			{@link ProbVector} storing the summed rolls and their probabilities
	 */
	public ProbVector flatten()
	{
		return flatten(sumFlatten);
	}
			
	private static final Function<Integer[], Integer> sumFlatten = new Function<Integer[], Integer>()
			{
				@Override
				public Integer apply(Integer[] key)
				{
					int total = 0;
					
					for (Integer myInt : key)
						total += myInt;
					
					return total;
				}
			};
}
