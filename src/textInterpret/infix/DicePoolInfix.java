package textInterpret.infix;

import diceTools.DiceRollVector;
import textInterpret.TokenFuncInputTypeException;
import textInterpret.TokenInfix;

public class DicePoolInfix extends TokenInfix
{

	//TODO: implement variable number of dice and sides

	@Override
	public Object operate(Object objA, Object objB)
	{
		if (objA == null || objB == null)
			throw new NullPointerException();
		
		if (!(objA instanceof Number) || !(objB instanceof Number))
			throw new TokenFuncInputTypeException();
		
		int numDice = ((Number) objA).intValue();
		int numSides = ((Number) objB).intValue();
		
		return DiceRollVector.diceRoll(numDice, numSides);
	}
	
	@Override
	public String getName()
	{
		return "D";
	}
}