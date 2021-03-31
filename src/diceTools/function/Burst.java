package diceTools.function;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.DicePoolMap;
import diceTools.ImmutableList;
import diceTools.ProbMap;
import diceTools.DiceRollMap;

public class Burst implements Function<List<Integer>, ProbMap<? extends List<Integer>>>
{
	private final List<Integer> matchList;
	private final DiceRollMap explodeOptions;
	
	public Burst(List<? extends Integer> matchList, DiceRollMap explodeOptions)
	{
		this.matchList = new ImmutableList<Integer>(matchList);
		this.explodeOptions = new DiceRollMap(explodeOptions);
	}
	
	public Burst(List<? extends Integer> matchList, int numSides)
	{
		this.matchList = new ImmutableList<Integer>(matchList);
		this.explodeOptions = DiceRollMap.diceRoll(1, numSides);
	}

	@Override
	public DicePoolMap apply(List<Integer> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		DicePoolMap dpm = new DicePoolMap();
		
		if (li.isEmpty())
			return dpm;
		
		Integer checkInt = li.get(0);
		
		if (matchList.contains(checkInt))
			dpm = new DicePoolMap(explodeOptions);
		else
		{
			List<Integer> key = new LinkedList<Integer>();
			key.add(checkInt);
			dpm.put(key, 1.0);
		}
		
		for (int checkPos = 1; checkPos < matchList.size(); checkPos++)
		{
			checkInt = li.get(checkPos);
			
			if (matchList.contains(checkInt))
				dpm = dpm.combine(explodeOptions);
			else
			{
				Append<Integer> app = new Append<Integer>(checkInt);
				dpm = (DicePoolMap) dpm.morph(app);
			}
		}
		
		return dpm;
	}

}
