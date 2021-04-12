package textInterpret.unary;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
import textInterpret.TokenFuncInputTypeException;

public class FlattenUnary extends ArgSortedUnary
{

	@Override
	public DiceRollMap operateCase(DicePoolMap operand)
	{
		return operand.flatten();
	}

	@Override
	public Object operateCase(DiceRollMap operand)
	{
		throw new TokenFuncInputTypeException(String.format("The argument to %s.operate cannot be of type %s",
				getClass().getName(), DiceRollMap.class.getName()));
	}

	@Override
	public Object operateCase(DiceNumber operand)
	{
		throw new TokenFuncInputTypeException(String.format("The argument to %s.operate cannot be of type %s",
				getClass().getName(), DiceNumber.class.getName()));
	}

	@Override
	public String getName()
	{
		return "!";
	}

}
