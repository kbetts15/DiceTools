package diceTools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

/**
 * Generates iterators to iterate all possible combinations of dice rolls.
 * Each combination is returned as a {@link java.util.Map.Entry Map.Entry},
 * where the key is a <code>List</code> with all the contained rolls in
 * descending order. The value of the <code>Entry</code> is the probability
 * of rolling that combination of dice. Each combination is iterated exactly once.
 * 
 * @author kieran
 */
public class DiceRollIterable implements Iterable<Entry<List<DiceNumber.DiceInteger>, Double>>
{
	/**
	 * Number of dice to roll
	 */
	private final int numDice;
	
	/**
	 * Number of sides on rolled dice
	 */
	private final int numSides;
	
	public DiceRollIterable(int numDice, int numSides)
	{
		this.numDice = numDice;
		this.numSides = numSides;
	}
	
	@Override
	public Iterator<Entry<List<DiceNumber.DiceInteger>, Double>> iterator()
	{
		return new DiceRollIterator();
	}
	
	/**
	 * Iterates all possible combinations of dice rolls for a
	 * {@link diceTools.DiceRollIterable DiceRollIterable instance}.
	 * 
	 * @author kieran
	 */
	private class DiceRollIterator implements Iterator<Entry<List<DiceNumber.DiceInteger>, Double>>
	{
		/**
		 * Last generated roll combination
		 */
		private final Integer[] lastRoll;
		
		/**
		 * The maximum number of possible distinct orderings of any
		 * given roll combination.
		 */
		private final int maxOrderings;
		
		/**
		 * The total possible distinct roll permutations.
		 */
		private final int possibleRolls;
		
		public DiceRollIterator()
		{
			lastRoll = new Integer[numDice];
			
			if (numDice > 0)
			{
				Arrays.fill(lastRoll, 1);
				lastRoll[0] = 0;
			}
			
			maxOrderings = factorial(numDice);
			possibleRolls = (int) Math.pow(numSides, numDice);
		}

		@Override
		public boolean hasNext()
		{
			return (numDice > 0) && (lastRoll[numDice - 1] != numSides);
		}

		@Override
		public Entry<List<DiceNumber.DiceInteger>, Double> next()
		{
			nextRoll(lastRoll);
			
			List<Integer> intList = Arrays.asList(lastRoll);
			List<DiceNumber.DiceInteger> keyList = new LinkedList<DiceNumber.DiceInteger>();

			Consumer<Integer> adder = new Consumer<Integer>() {

				@Override
				public void accept(Integer x)
				{
					keyList.add(new DiceNumber.DiceInteger(x));
				}
				
			};
			
			intList.forEach(adder);
			
			double prob = getProb(lastRoll);

			return new SimpleEntry<List<DiceNumber.DiceInteger>, Double>(keyList, prob);
		}
		
		/**
		 * Generate the next roll combination
		 * @param roll		previous roll combination - must be in descending order
		 */
		private void nextRoll(Integer roll[])
		{
			if (!hasNext())
				throw new NoSuchElementException("No next roll to go to");
			
			int incPos = 0;
			
			while (roll[incPos] == numSides)
				incPos++;
			
			roll[incPos]++;
			
			if (incPos > 0)
				for (int i = incPos; i >= 0; i--)
					roll[i] = roll[incPos];
		}
		
		/**
		 * Calculate the probability of getting a given roll combination
		 * on rolling <code>numDice</code> identical unbiased dice with
		 * <code>numSides</code> sides.
		 * @param roll		roll combination - must be in descending order
		 * @return			the probability of rolling the given roll combination
		 */
		private double getProb(Integer[] roll)
		{
			int divOrderings = 1;
			int count = 0;
			int lastRoll = 0;
			
			for (Integer r : roll)
			{
				count = (r == lastRoll) ? (count + 1) : 1;
				lastRoll = r;
				divOrderings *= count;
			}
			
			return ((double) maxOrderings) / (divOrderings * possibleRolls);
		}
		
		/**
		 * Calculate the mathematical factorial of an <code>int</code>
		 * @param in	x
		 * @return		x!
		 */
		private int factorial(int in)
		{
			int out = in;
			for (int i = in - 1; i > 1; i--)
				out *= i;
			return out;
		}
	}
}