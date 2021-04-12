package textInterpret.function;

import java.util.LinkedList;
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
		if (li == null || li.isEmpty())
			return new DiceRollMap();
		
		final Object objFirst = li.get(0);
		
		if (objFirst instanceof KeyValuePair)
		{
			DiceRollMap roll = new DiceRollMap();
			
			for (Object obj : li)
			{
				if (!(obj instanceof KeyValuePair))
					throw new TokenFuncInputTypeException(String.format("Dice roll construct must contain only numbers"
							+ "or only number-probability pairs - innapropriate Object type found in pair list (%s)",
							obj.getClass().getName()));
				
				KeyValuePair kvp = (KeyValuePair) obj;
				
				if (!(kvp.getKey() instanceof DiceNumber))
					throw new TokenFuncInputTypeException(String.format("Dice roll construct must contain only numbers"
							+ "or only number-probability pairs - pair with invalid key type found (%s)",
							kvp.getValue().getClass().getName()));
				
				if (!(kvp.getKey() instanceof Number))
					throw new TokenFuncInputTypeException(String.format("Dice roll construct must contain only numbers"
							+ "or only number-probability pairs - pair with invalid value type found (%s)",
							kvp.getKey().getClass().getName()));
				
				roll.put((DiceNumber) kvp.getKey(), ((Number) kvp.getValue()).doubleValue());
			}
			
			return roll;
		}
		else if (objFirst instanceof DiceNumber)
		{
			List<DiceNumber> numbers = new LinkedList<DiceNumber>();
			
			for (Object obj : li)
			{
				if (!(obj instanceof DiceNumber))
					throw new TokenFuncInputTypeException(String.format("Dice roll construct must contain only numbers"
							+ "or only number-probability pairs - innapropriate Object found in number list (%s)",
							obj.getClass().getName()));
				
				numbers.add((DiceNumber) obj);
			}
			
			DiceRollMap roll = new DiceRollMap();
			double prob = 1.0 / numbers.size();
			
			for (DiceNumber n : numbers)
				roll.merge(n, prob);
			
			return roll;
		}
		else
			throw new TokenFuncInputTypeException(String.format("Dice roll construct must contain only numbers"
					+ "or only number-probability pairs - first element has invalid type (%s)",
					objFirst.getClass().getName()));
		
	}

	@Override
	public String getName()
	{
		return "d";
	}
	
}
