package diceTools.function;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.ImmutableList;
import diceTools.ProbMap;
import diceTools.DiceRollMap;

//TODO: Burst JavaDoc
public class Burst implements Function<List<? extends DiceNumber>, ProbMap<? extends List<? extends DiceNumber>>>
{
	private final List<DiceNumber> matchList;
	private final DiceRollMap explodeOptions;
	
	public Burst(List<? extends DiceNumber> matchList, DiceRollMap explodeOptions)
	{
		this.matchList = new ImmutableList<DiceNumber>(matchList);
		this.explodeOptions = new DiceRollMap(explodeOptions);
	}
	
	public Burst(List<? extends DiceNumber> matchList, int numSides)
	{
		this.matchList = new ImmutableList<DiceNumber>(matchList);
		this.explodeOptions = DiceRollMap.diceRoll(1, numSides);
	}

	@Override
	public DicePoolMap apply(List<? extends DiceNumber> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		DicePoolMap dpm = new DicePoolMap();
		
		if (li.isEmpty())
			return dpm;
		
		DiceNumber checkInt = li.get(0);
		
		if (matchList.contains(checkInt))
			dpm = new DicePoolMap(explodeOptions);
		else
		{
			List<DiceNumber> key = new LinkedList<DiceNumber>();
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
				Append<DiceNumber> app = new Append<DiceNumber>(checkInt);
				dpm = (DicePoolMap) dpm.morph(app);
			}
		}
		
		return dpm;
	}

}
