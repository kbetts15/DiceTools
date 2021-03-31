package textInterpret.function;

import java.util.LinkedList;
import java.util.List;

import diceTools.DicePoolMap;
import textInterpret.KeyValuePair;
import textInterpret.TokenFunc;
import textInterpret.TokenFuncInputTypeException;

public class DicePoolFunc extends TokenFunc
{

	@Override
	public Object operate(List<? extends Object> li)
	{
		List<Integer> rollList = new LinkedList<Integer>();
		DicePoolMap dicePool = new DicePoolMap();
		
		for (Object obj : li)
		{
			if (obj instanceof Integer)
				rollList.add((int) obj);
			else if (obj instanceof KeyValuePair)
			{
				KeyValuePair kvp = (KeyValuePair) obj;
				
				if ((kvp.getKey() instanceof Integer) && (kvp.getValue() instanceof Number))
				{
					rollList.add((int) kvp.getKey());
					
					if (dicePool.containsKey(rollList))
						throw new RuntimeException("Duplicate key in dice pool construct");
					
					dicePool.put(rollList, (double) kvp.getValue());
					rollList = new LinkedList<Integer>();
				}
			}
			else
				throw new TokenFuncInputTypeException();
		}
		
		if (!rollList.isEmpty())
			throw new RuntimeException("Dice pool construct cannot contain keys without values");
		
		return dicePool;
	}

	@Override
	public String getName()
	{
		return "D";
	}

}
