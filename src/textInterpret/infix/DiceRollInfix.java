package textInterpret.infix;

import diceTools.DiceRollMap;
import diceTools.ProbMap;

public class DiceRollInfix extends RollingInfix<Integer>
{
	
	public DiceRollInfix()
	{
		super(new DiceRollMap());
	}

	@Override
	public ProbMap<Integer> operateCase(Integer a, Integer b)
	{
		return DiceRollMap.diceRoll(a, b);
	}
	
	@Override
	public String getName()
	{
		return "d";
	}
}
