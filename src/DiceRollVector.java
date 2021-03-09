import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
public class DiceRollVector extends ProbMap<List<Integer>> //TODO: use immutable lists for storage!
{
	private static final long serialVersionUID = 1L;
	
	public DiceRollVector()
	{
		super(new LinkedList<Integer>());
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
		return sanitizedKey;
	}
	
	@Override
	public Double get(Object key)
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
			
			for (Integer pvInt : pv.keySet())
			{
				List<Integer> listNew = new LinkedList<Integer>();
				listNew.add(pvInt);
				drvNew.put(listNew, pv.get(pvInt));
			}
			
			return drvNew;
		}
		
//		System.out.println("Combining:");
//		System.out.printf("\t%s\n", this.toString());
//		System.out.printf("\t%s\n", pv.toString());
//		System.out.println("Results:");
		
		for (List<Integer> myList : this.keySet())
		{
			for (Integer pvInt : pv.keySet())
			{
				List<Integer> newList = new LinkedList<Integer>(myList);
				newList.add(pvInt);
				
				drvNew.merge(newList, drvNew.get(newList), sumMerger);
				System.out.printf("\t%s\n", drvNew.toString());
			}
		}
		
		return drvNew;
	}
	
	public DiceRollVector morph(Function<List<Integer>, List<Integer>> function)
	{
		DiceRollVector drvNew = new DiceRollVector();
		
		for (List<Integer> keyList : this.keySet())
		{
			List<Integer> newList = function.apply(keyList);
			
			if (!keyIsValid(newList))
				continue;
			
			drvNew.merge(newList, this.get(keyList), ProbVector.sumMerger);
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
	
	//TODO: instantiate sumFlatten in a static block
	private static final Function<List<Integer>, Integer> sumFlatten = new Function<List<Integer>, Integer>()
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
