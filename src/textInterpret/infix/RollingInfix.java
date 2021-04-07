package textInterpret.infix;

import java.util.Map;
import java.util.function.Supplier;

import diceTools.DiceNumber;
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
	
	@Override public ProbMap<T> operateCase(DicePoolMap a,	DicePoolMap b)	{return operateCase(a.flatten(), b.flatten());}
	@Override public ProbMap<T> operateCase(DicePoolMap a,	DiceRollMap b)	{return operateCase(a.flatten(), b);}
	@Override public ProbMap<T> operateCase(DicePoolMap a,	DiceNumber b)	{return operateCase(a.flatten(), b);}
	@Override public ProbMap<T> operateCase(DiceRollMap a,	DicePoolMap b)	{return operateCase(a, b.flatten());}
	@Override public ProbMap<T> operateCase(DiceNumber a,	DicePoolMap b)	{return operateCase(a, b.flatten());}

	@Override
	public ProbMap<T> operateCase(DiceRollMap a, DiceRollMap b)
	{
		ProbMap<T> outRoll = supplier.get();
		
		for (Map.Entry<DiceNumber, Double> entryA : a.entrySet())
			for (Map.Entry<DiceNumber, Double> entryB : b.entrySet())
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
	public ProbMap<T> operateCase(DiceRollMap a, DiceNumber b)
	{
		ProbMap<T> outRoll = supplier.get();
		
		for (Map.Entry<DiceNumber, Double> entry : a.entrySet())
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
	public ProbMap<T> operateCase(DiceNumber a, DiceRollMap b)
	{
		ProbMap<T> outRoll = supplier.get();
		
		for (Map.Entry<DiceNumber, Double> entry : b.entrySet())
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
	public abstract ProbMap<T> operateCase(DiceNumber a, DiceNumber b);

}
