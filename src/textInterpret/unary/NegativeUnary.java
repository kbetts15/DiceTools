package textInterpret.unary;

import diceTools.DiceNumber;

public class NegativeUnary extends NumericUnary
{
	
	@Override
	public DiceNumber operateCase(DiceNumber operand)
	{
		if (operand.isInt())
			return new DiceNumber.DiceInteger(-operand.intValue());
		else
			return new DiceNumber.DiceDouble(-operand.doubleValue());
	}

	@Override
	public String getName()
	{
		return "-";
	}

}
