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
	private static final BiFunction<List<Integer>, Integer, List<Integer>> listAdd;
	
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
		DiceRollVector result = new DiceRollVector();
		
		ProbVector die = ProbVector.diceRoll(1, sides);
		
		for (int i = 0; i < numDice; i++)
			result = result.combine(die);
		
		return result;
	}
	
	//TODO: reimplement combine using ProbMap.combine
	
	/**
	 * Generate all the possible results of combining the roll arrays from the calling
	 * {@link DiceRollVector} with each possible roll from the given {@link ProbVector}
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
	 * Combine the rolls in each roll array to a single Integer
	 * @param function	function which combines the rolls
	 * @return			{@link ProbVector} storing the combined rolls and their probabilities
	 */
	public ProbVector flatten(Function<List<Integer>, Integer> function)
	{
		return (ProbVector) super.morphSingle(function, new ProbVector().supplyMe());
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
