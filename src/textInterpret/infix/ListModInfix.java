package textInterpret.infix;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
import textInterpret.TokenFuncInputTypeException;
import textInterpret.TokenInfix;

public abstract class ListModInfix extends TokenInfix
{
	
	@Override
	public Object operate(Object objA, Object objB)
	{
		if (objA instanceof DicePoolMap)
		{
			if (objB instanceof Integer)
				return operateSingle((DicePoolMap) objA, (Integer) objB);
			else if (objB instanceof DiceRollMap)
				return operateProb((DicePoolMap) objA, (DiceRollMap) objB);
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof DiceRollMap)
			return operate(new DicePoolMap((DiceRollMap) objA), objB);
		else
			throw new TokenFuncInputTypeException();
	}
	
	private DicePoolMap operateSingle(DicePoolMap mapIn, Integer intMod)
	{
		Function<? super List<Integer>, ? extends List<Integer>> liMod = listMod(intMod);
		return (DicePoolMap) mapIn.morph(liMod);
	}
	
	private DicePoolMap operateProb(DicePoolMap mapIn, DiceRollMap mapMod)
	{
		DicePoolMap outPool = new DicePoolMap();
		
		for (Entry<Integer, Double> entry : mapMod.entrySet())
		{
			Function<? super List<Integer>, ? extends List<Integer>> liMod = listMod(entry.getKey());
			DicePoolMap roll = new DicePoolMap();
			
			mapIn.forEach((key, value) -> {
				roll.put(liMod.apply(key), value * entry.getValue());
			});
			
			outPool.mergeAll(roll);
		}
		
		return outPool;
	}
	
	public abstract Function<? super List<Integer>, ? extends List<Integer>> listMod(Integer intMod);
	
}
