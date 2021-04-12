package textInterpret.infix;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import diceTools.DiceNumber;
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
			if (objB instanceof DiceNumber)
				return operateSingle((DicePoolMap) objA, (DiceNumber) objB);
			else if (objB instanceof DiceRollMap)
				return operateProb((DicePoolMap) objA, (DiceRollMap) objB);
			else
				throw new TokenFuncInputTypeException(String.format("The second argument to %s.operate must be of type %s or %s, not %s",
						getClass().getName(), DiceRollMap.class.getName(), DicePoolMap.class.getName(), objB.getClass().getName()));
		}
		else if (objA instanceof DiceRollMap)
			return operate(new DicePoolMap((DiceRollMap) objA), objB);
		else
			throw new TokenFuncInputTypeException(String.format("The first argument to %s.operate must be of type %s or %s, not %s",
					getClass().getName(), DiceRollMap.class.getName(), DicePoolMap.class.getName(), objA.getClass().getName()));
	}
	
	private DicePoolMap operateSingle(DicePoolMap mapIn, DiceNumber intMod)
	{
		Function<? super List<? extends DiceNumber>, ? extends List<DiceNumber>> liMod = listMod(intMod.intValue());
		//TODO: this makes no allowance for double-based operations - is this important?
		return (DicePoolMap) mapIn.morph(liMod);
	}
	
	private DicePoolMap operateProb(DicePoolMap mapIn, DiceRollMap mapMod)
	{
		DicePoolMap outPool = new DicePoolMap();
		
		for (Entry<DiceNumber, Double> entry : mapMod.entrySet())
		{
			Function<? super List<? extends DiceNumber>, List<DiceNumber>> liMod = listMod(entry.getKey().intValue());
			//TODO: this makes no allowance for double-based operations - is this important?
			DicePoolMap roll = new DicePoolMap();
			
			mapIn.forEach((key, value) -> {
				roll.put(liMod.apply(key), value * entry.getValue());
			});
			
			outPool.mergeAll(roll);
		}
		
		return outPool;
	}
	
	public abstract Function<? super List<? extends DiceNumber>, List<DiceNumber>> listMod(int intMod);
	
}
