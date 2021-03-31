package textInterpret.unary;

import diceTools.DicePoolMap;
import diceTools.ProbVector;
import textInterpret.TokenFuncInputTypeException;

public class FlattenUnary extends ArgSortedUnary
{

	@Override
	public Object operateCase(DicePoolMap operand)
	{
		return operand.flatten();
	}

	@Override
	public Object operateCase(ProbVector operand)
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
