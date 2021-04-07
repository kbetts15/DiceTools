package textInterpret.infix;

import java.util.List;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;

public class DicePoolInfix extends RollingInfix<List<? extends DiceNumber>>
{
	
	public DicePoolInfix()
	{
		super(new DicePoolMap());
	}

	@Override
	public DicePoolMap operateCase(DiceNumber a, DiceNumber b)
	{
		//TODO: implement floor, ceil, and round unary functions
		
		if (!a.isInt() || !b.isInt())
			throw new RuntimeException("Cannot create dice pool from non-integers"); //TODO: write a proper exception class for this
		
		return DicePoolMap.diceRoll(a.intValue(), b.intValue());
	}
	
	@Override
	public String getName()
	{
		return "D";
	}
}
