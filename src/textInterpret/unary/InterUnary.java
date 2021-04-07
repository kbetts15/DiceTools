package textInterpret.unary;

import diceTools.DiceNumber;

public class InterUnary extends NumericUnary
{

	@Override
	public DiceNumber operateCase(DiceNumber operand)
	{
		return new DiceNumber.DiceInteger((int) Math.round(operand.doubleValue()));
	}

	@Override
	public String getName()
	{
		return "'";
	}

}
