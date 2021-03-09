import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
public class DiceRollVector extends ProbMap<List<Integer>> //TODO: use immutable lists for storage!
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums each <code>Integer</code> in a <code>List&ltInteger&gt</code> key
	 */
	private static final Function<List<Integer>, Integer> sumFlatten;
	
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
	public Double get(Object key) //TODO: do this for other methods? (eg remove)
	{
		if ((key instanceof Integer[]) || (key instanceof int[]))
		{
			Integer[] keyArr = (Integer[]) key;
			return super.get(Arrays.asList(keyArr));
		}

		return super.get(key);
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
			for (Entry<Integer, Double> entry : pv.entrySet())
			{
				List<Integer> listNew = new LinkedList<Integer>();
				listNew.add(entry.getKey());
				drvNew.put(listNew, entry.getValue());
			}
			
			return drvNew;
		}
		
		for (Entry<List<Integer>, Double> myEntry : this.entrySet())
		{
			final List<Integer> myList = myEntry.getKey();
			final Double myProb = myEntry.getValue();
			
			for (Entry<Integer, Double> pvEntry : pv.entrySet())
			{
				final Integer pvInt = pvEntry.getKey();
				final Double pvProb = pvEntry.getValue();
				
				List<Integer> newList = new LinkedList<Integer>(myList);
				newList.add(pvInt);
				
				Double newProb = myProb * pvProb;
				
				drvNew.merge(newList, newProb, sumMerger);
			}
		}
		
		return drvNew;
	}
	
	/**
	 * Generate a new DiceRollVector by transforming each key into another
	 * @param function	specifies the rule for transforming keys
	 * @return			DiceRollVector with resulting transformed keys
	 */
	public DiceRollVector morph(Function<List<Integer>, List<Integer>> function)
	{
		DiceRollVector drvNew = new DiceRollVector();
		
		for (Entry<List<Integer>, Double> entry : this.entrySet())
		{
			List<Integer> newList = function.apply(entry.getKey());
			
			drvNew.merge(newList, entry.getValue(), sumMerger);
		}
		
		return drvNew;
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @param function	function which combines the rolls
	 * @return			{@link ProbVector} storing the combined rolls and their probabilities
	 */
	public ProbVector flatten(Function<List<Integer>, Integer> function)
	{
		ProbVector pvNew = new ProbVector();
		
		for (List<Integer> myArr : this.keySet())
		{
			Integer newInt = function.apply(myArr); //TODO: use entrySet and merge
			
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
}
