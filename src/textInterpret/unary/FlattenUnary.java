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
		throw new TokenFuncInputTypeException();
	}

	@Override
	public Object operateCase(DiceNumber operand)
	{
		throw new TokenFuncInputTypeException();
	}

	@Override
	public String getName()
	{
		return "!";
	}

}
