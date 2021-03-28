package textInterpret.unary;

import diceTools.DiceRollVector;
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
		else if (obj instanceof DiceRollVector)
			return operateCase((DiceRollVector) obj);
		else
			throw new TokenFuncInputTypeException();
	}

	public abstract Object operateCase(DiceRollVector operand);
	public abstract Object operateCase(ProbVector operand);
	public abstract Object operateCase(Integer operand);
}
