package textInterpret.unary;

import diceTools.DicePoolMap;
import diceTools.ProbVector;
import textInterpret.TokenFuncInputTypeException;
import textInterpret.TokenUnary;

public abstract class ArgSortedUnary extends TokenUnary
{

	@Override
	public Object operate(Object obj)
	{
		if (obj instanceof Integer)
			return operateCase((Integer) obj);
		else if (obj instanceof ProbVector)
			return operateCase((ProbVector) obj);
		else if (obj instanceof DicePoolMap)
			return operateCase((DicePoolMap) obj);
		else
			throw new TokenFuncInputTypeException();
	}

	public abstract Object operateCase(DicePoolMap operand);
	public abstract Object operateCase(ProbVector operand);
	public abstract Object operateCase(Integer operand);
}
