package diceTools.function;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceRollVector;
import diceTools.ImmutableList;
import diceTools.ProbMap;
import diceTools.ProbVector;

public class Burst implements Function<List<Integer>, ProbMap<? extends List<Integer>>>
{
	private final List<Integer> matchList;
	private final ProbVector explodeOptions;
	
	public Burst(List<? extends Integer> matchList, ProbVector explodeOptions)
	{
		this.matchList = new ImmutableList<Integer>(matchList);
		this.explodeOptions = new ProbVector(explodeOptions);
	}
	
	public Burst(List<? extends Integer> matchList, int numSides)
	{
		this.matchList = new ImmutableList<Integer>(matchList);
		this.explodeOptions = ProbVector.diceRoll(1, numSides);
	}

	@Override
	public DiceRollVector apply(List<Integer> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		DiceRollVector drv = new DiceRollVector();
		
		if (li.isEmpty())
			return drv;
		
		Integer checkInt = li.get(0);
		
		if (matchList.contains(checkInt))
			drv = new DiceRollVector(explodeOptions);
		else
		{
			List<Integer> key = new LinkedList<Integer>();
			key.add(checkInt);
			drv.put(key, 1.0);
		}
		
		for (int checkPos = 1; checkPos < matchList.size(); checkPos++)
		{
			checkInt = li.get(checkPos);
			
			if (matchList.contains(checkInt))
				drv = drv.combine(explodeOptions);
			else
			{
				Append<Integer> app = new Append<Integer>(checkInt);
				drv = (DiceRollVector) drv.morph(app);
			}
		}
		
		return drv;
	}

}
