package textInterpret.function;

import java.util.List;

import diceTools.DiceNumber;
import diceTools.DiceRollMap;
import textInterpret.KeyValuePair;
import textInterpret.TokenFunc;
import textInterpret.TokenFuncInputTypeException;

public class DiceRollFunc extends TokenFunc
{

	@Override
	public Object operate(List<? extends Object> li)
	{
		DiceRollMap rollMap = new DiceRollMap();
		for (Object obj : li)
		{
			KeyValuePair kvp;
			if (obj instanceof KeyValuePair)
				kvp = (KeyValuePair) obj;
			else
				throw new TokenFuncInputTypeException();
			
			if ((kvp.getKey() instanceof DiceNumber)
					&& (kvp.getValue() instanceof Double))
				rollMap.put((DiceNumber) kvp.getKey(), (Double) kvp.getValue());
			else
				throw new TokenFuncInputTypeException();
		}
		return rollMap;
	}

	@Override
	public String getName()
	{
		return "d";
	}
	
}
