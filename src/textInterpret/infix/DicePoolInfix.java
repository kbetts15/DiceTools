package textInterpret.infix;

import java.util.Map;

import diceTools.DicePoolMap;
import diceTools.DiceRollMap;

public class DicePoolInfix extends ArgSortedInfix
{

	@Override
	public DicePoolMap operateCase(DicePoolMap a, DicePoolMap b)
	{
		return operateCase(a.flatten(), b.flatten());
	}

	@Override
	public DicePoolMap operateCase(DicePoolMap a, DiceRollMap b)
	{
		return operateCase(a.flatten(), b);
	}

	@Override
	public DicePoolMap operateCase(DicePoolMap a, Integer b)
	{
		return operateCase(a.flatten(), b);
	}

	@Override
	public DicePoolMap operateCase(DiceRollMap a, DicePoolMap b)
	{
		return operateCase(a, b.flatten());
	}

	@Override
	public DicePoolMap operateCase(DiceRollMap a, DiceRollMap b)
	{
		DicePoolMap outPool = new DicePoolMap();
		
		for (Map.Entry<Integer, Double> entryA : a.entrySet())
		{
			for (Map.Entry<Integer, Double> entryB : b.entrySet())
			{
				DicePoolMap rollPool = (DicePoolMap) operateCase(entryA.getKey(), entryB.getKey());
				
				final double normalisingFactor = entryA.getValue() * entryB.getValue();
				DicePoolMap normalisedPool = new DicePoolMap();
				
				rollPool.forEach((key, value) -> {normalisedPool.put(key, value * normalisingFactor);});
				
				outPool.mergeAll(normalisedPool);
			}
		}
		
		return outPool;
	}

	@Override
	public DicePoolMap operateCase(DiceRollMap a, Integer b)
	{
		DicePoolMap outPool = new DicePoolMap();
		
		for (Map.Entry<Integer, Double> entry : a.entrySet())
		{
			DicePoolMap rollPool = (DicePoolMap) operateCase(entry.getKey(), b);
			
			final double normalisingFactor = entry.getValue();
			DicePoolMap normalisedPool = new DicePoolMap();
			
			rollPool.forEach((key, value) -> {normalisedPool.put(key, value * normalisingFactor);});
			
			outPool.mergeAll(normalisedPool);
		}
		
		return outPool;
	}

	@Override
	public DicePoolMap operateCase(Integer a, DicePoolMap b)
	{
		return operateCase(a, b.flatten());
	}

	@Override
	public DicePoolMap operateCase(Integer a, DiceRollMap b)
	{
		DicePoolMap outPool = new DicePoolMap();
		
		for (Map.Entry<Integer, Double> entry: b.entrySet())
		{
			DicePoolMap rollPool = (DicePoolMap) operateCase(a, entry.getKey());

			final double normalisingFactor = entry.getValue();
			DicePoolMap normalisedPool = new DicePoolMap();
			
			rollPool.forEach((key, value) -> {normalisedPool.put(key, value * normalisingFactor);});
			
			outPool.mergeAll(normalisedPool);
		}
		
		return outPool;
	}

	@Override
	public DicePoolMap operateCase(Integer a, Integer b)
	{
		int numDice = ((Number) a).intValue();
		int numSides = ((Number) b).intValue();
		
		return DicePoolMap.diceRoll(numDice, numSides);
	}
	
	@Override
	public String getName()
	{
		return "D";
	}
}
