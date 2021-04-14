package textInterpret.unary;

import diceTools.DiceNumber;

public class RoundUnary extends NumericUnary
{

	@Override
	public DiceNumber operateCase(DiceNumber operand)
	{
		if (operand == null || operand.isInt())
			return operand;
		else
			return new DiceNumber.DiceInteger((int) Math.round(operand.doubleValue()));
	}

	@Override
	public String getName()
	{
		return "R";
	}
	
}
