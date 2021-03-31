package textInterpret.unary;

import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
import textInterpret.TokenFuncInputTypeException;
import textInterpret.TokenUnary;

public abstract class ArgSortedUnary extends TokenUnary
{

	@Override
	public Object operate(Object obj)
	{
		if (obj instanceof Integer)
			return operateCase((Integer) obj);
		else if (obj instanceof DiceRollMap)
			return operateCase((DiceRollMap) obj);
		else if (obj instanceof DicePoolMap)
			return operateCase((DicePoolMap) obj);
		else
			throw new TokenFuncInputTypeException();
	}

	public abstract Object operateCase(DicePoolMap operand);
	public abstract Object operateCase(DiceRollMap operand);
	public abstract Object operateCase(Integer operand);
}
