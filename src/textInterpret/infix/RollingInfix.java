package textInterpret.infix;

import java.util.Map;
import java.util.function.Supplier;

import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
import diceTools.ProbMap;

public abstract class RollingInfix<T> extends ArgSortedInfix
{
	
	private final Supplier<ProbMap<T>> supplier;
	
	public RollingInfix(ProbMap<T> supplier)
	{
		this.supplier = supplier;
	}
	
	@Override public Object operateCase(DicePoolMap a, DicePoolMap b)	{return operateCase(a.flatten(), b.flatten());}
	@Override public Object operateCase(DicePoolMap a, DiceRollMap b)	{return operateCase(a.flatten(), b);}
	@Override public Object operateCase(DicePoolMap a, Integer b)		{return operateCase(a.flatten(), b);}
	@Override public Object operateCase(DiceRollMap a, DicePoolMap b)	{return operateCase(a, b.flatten());}
	@Override public Object operateCase(Integer a, DicePoolMap b)		{return operateCase(a, b.flatten());}

	@Override
	public Object operateCase(DiceRollMap a, DiceRollMap b)
	{
		ProbMap<T> outRoll = supplier.get();
		
		for (Map.Entry<Integer, Double> entryA : a.entrySet())
			for (Map.Entry<Integer, Double> entryB : b.entrySet())
			{
				ProbMap<T> roll = operateCase(entryA.getKey(), entryB.getKey());
				
				final double normalisingFactor = entryA.getValue() * entryB.getValue();
				ProbMap<T> normalisedRoll = supplier.get();
				
				roll.forEach((key, value) -> {normalisedRoll.put(key, value * normalisingFactor);});
				outRoll.mergeAll(normalisedRoll);
			}
		
		return outRoll;
	}

	@Override
	public Object operateCase(DiceRollMap a, Integer b)
	{
		ProbMap<T> outRoll = supplier.get();
		
		for (Map.Entry<Integer, Double> entry : a.entrySet())
		{
			ProbMap<T> roll = operateCase(entry.getKey(), b);
			
			final double normalisingFactor = entry.getValue();
			ProbMap<T> normalisedRoll = supplier.get();
			
			roll.forEach((key, value) -> {normalisedRoll.put(key, value * normalisingFactor);});
			outRoll.mergeAll(normalisedRoll);
		}
		
		return outRoll;
	}

	@Override
	public Object operateCase(Integer a, DiceRollMap b)
	{
		ProbMap<T> outRoll = supplier.get();
		
		for (Map.Entry<Integer, Double> entry : b.entrySet())
		{
			ProbMap<T> roll = operateCase(a, entry.getKey());
			
			final double normalisingFactor = entry.getValue();
			ProbMap<T> normalisedRoll = supplier.get();
			
			roll.forEach((key, value) -> {normalisedRoll.put(key, value * normalisingFactor);});
			outRoll.mergeAll(normalisedRoll);
		}
		
		return outRoll;
	}
	
	@Override
	public abstract ProbMap<T> operateCase(Integer a, Integer b);

}
