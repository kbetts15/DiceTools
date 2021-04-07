package textInterpret.unary;

import diceTools.DiceNumber;

public class FloaterUnary extends NumericUnary
{

	@Override
	public DiceNumber operateCase(DiceNumber operand)
	{
		return new DiceNumber.DiceDouble(operand.doubleValue());
	}

	@Override
	public String getName()
	{
		return "\"";
	}

}
