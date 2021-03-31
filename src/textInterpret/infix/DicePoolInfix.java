package textInterpret.infix;

import java.util.List;

import diceTools.DicePoolMap;
import diceTools.ProbMap;

public class DicePoolInfix extends RollingInfix<List<Integer>>
{
	
	public DicePoolInfix()
	{
		super(new DicePoolMap());
	}

	@Override
	public ProbMap<List<Integer>> operateCase(Integer a, Integer b)
	{
		return DicePoolMap.diceRoll(a, b);
	}
	
	@Override
	public String getName()
	{
		return "D";
	}
}
