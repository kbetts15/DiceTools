package textInterpret.infix;

import diceTools.DiceNumber;

public class AddInfix extends NumericInfix
{

	@Override
	public DiceNumber operateCase(DiceNumber a, DiceNumber b)
	{
		if (a.isInt() && b.isInt())
			return new DiceNumber.DiceInteger(a.intValue() + b.intValue());
		else
			return new DiceNumber.DiceDouble(a.doubleValue() + b.doubleValue());
	}

	@Override
	public String getName()
	{
		return "+";
	}
}
