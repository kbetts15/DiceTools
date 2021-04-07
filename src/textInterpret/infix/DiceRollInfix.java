package textInterpret.infix;

import diceTools.DiceNumber;
import diceTools.DiceRollMap;

public class DiceRollInfix extends RollingInfix<DiceNumber>
{
	
	public DiceRollInfix()
	{
		super(new DiceRollMap());
	}

	@Override
	public DiceRollMap operateCase(DiceNumber a, DiceNumber b)
	{
		if (!a.isInt() || !b.isInt())
			throw new RuntimeException("Cannot create dice roll from non-integers"); //TODO: write a proper exception class for this
		
		return DiceRollMap.diceRoll(a.intValue(), b.intValue());
	}
	
	@Override
	public String getName()
	{
		return "d";
	}
}
