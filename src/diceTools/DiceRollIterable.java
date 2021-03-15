package diceTools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class DiceRollIterable implements Iterable<Entry<List<Integer>, Double>>
{
	private final int numDice;
	private final int numSides;
	
	public DiceRollIterable(int numDice, int numSides)
	{
		this.numDice = numDice;
		this.numSides = numSides;
	}
	
	@Override
	public Iterator<Entry<List<Integer>, Double>> iterator()
	{
		return new DiceRollIterator();
	}
	
	private class DiceRollIterator implements Iterator<Entry<List<Integer>, Double>>
	{
		private final Integer[] rolls;
		
		public DiceRollIterator()
		{
			rolls = new Integer[numDice];
			
			if (numDice > 0)
			{
				Arrays.fill(rolls, 1);
				rolls[0] = 0;
			}
		}

		@Override
		public boolean hasNext()
		{
			return (numDice > 0) && (rolls[numDice - 1] != numSides);
		}

		@Override
		public Entry<List<Integer>, Double> next()
		{
			goToNextRoll();
			
			List<Integer> key = Arrays.asList(rolls);
			double prob = getProb(rolls);

			return new SimpleEntry<List<Integer>, Double>(key, prob);
		}
		
		private void goToNextRoll()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			
			int incPos = 0;
			
			while (rolls[incPos] == numSides)
				incPos++;
			
			rolls[incPos]++;
			
			if (incPos > 0)
				for (int i = incPos; i >= 0; i--)
					rolls[i] = rolls[incPos];
		}
		
		private double getProb(Integer[] rolls)
		{
			return 0.0; //TODO: write getProb
		}
	}
}