package textInterpret.infix;

import diceTools.DiceNumber;

public class PowerInfix extends NumericInfix
{

	@Override
	public DiceNumber operateCase(DiceNumber a, DiceNumber b)
	{
		if (a.isInt() && b.isInt())
			return new DiceNumber.DiceInteger((int) Math.round(Math.pow(a.intValue(), b.intValue())));
		else
			return new DiceNumber.DiceDouble(Math.pow(a.doubleValue(), b.doubleValue()));
	}

	@Override
	public String getName()
	{
		return "^";
	}

}
