package textInterpret.function;

import java.util.LinkedList;
import java.util.List;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import textInterpret.KeyValuePair;
import textInterpret.TokenFunc;
import textInterpret.TokenFuncInputTypeException;

public class DicePoolFunc extends TokenFunc
{

	@Override
	public Object operate(List<? extends Object> li)
	{
		List<DiceNumber> rollList = new LinkedList<DiceNumber>();
		DicePoolMap dicePool = new DicePoolMap();
		
		for (Object obj : li)
		{
			if (obj instanceof DiceNumber)
				rollList.add((DiceNumber) obj);
			else if (obj instanceof KeyValuePair)
			{
				KeyValuePair kvp = (KeyValuePair) obj;
				
				if ((kvp.getKey() instanceof DiceNumber) && (kvp.getValue() instanceof Number))
				{
					rollList.add((DiceNumber) kvp.getKey());
					
					if (dicePool.containsKey(rollList))
						throw new RuntimeException("Duplicate key in dice pool construct");
					
					dicePool.put(rollList, ((Number) kvp.getValue()).doubleValue());
					rollList = new LinkedList<DiceNumber>();
				}
			}
			else
				throw new TokenFuncInputTypeException(String.format("Dice pool construct must contain only numbers"
						+ "and number-probability pairs - innapropriate Object found (%s)",
						obj.getClass().getName()));
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
