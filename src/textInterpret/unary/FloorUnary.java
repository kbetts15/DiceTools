package textInterpret.unary;

import diceTools.DiceNumber;

public class FloorUnary extends NumericUnary
{

	@Override
	public DiceNumber operateCase(DiceNumber operand)
	{
		if (operand == null || operand.isInt())
			return operand;
		else
			return new DiceNumber.DiceInteger((int) Math.floor(operand.doubleValue()));
	}

	@Override
	public String getName()
	{
		return "F";
	}

}
