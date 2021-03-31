package textInterpret.unary;

import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
import textInterpret.TokenFuncInputTypeException;

public class FlattenUnary extends ArgSortedUnary
{

	@Override
	public Object operateCase(DicePoolMap operand)
	{
		return operand.flatten();
	}

	@Override
	public Object operateCase(DiceRollMap operand)
	{
		throw new TokenFuncInputTypeException();
	}

	@Override
	public Object operateCase(Integer operand)
	{
		throw new TokenFuncInputTypeException();
	}

	@Override
	public String getName()
	{
		return "!";
	}

}
