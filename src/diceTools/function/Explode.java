package diceTools.function;

import java.util.List;
import java.util.function.Function;

import diceTools.DiceRollVector;
import diceTools.ProbMap;
import diceTools.ProbVector;

public class Explode implements Function<List<Integer>, ProbMap<? extends List<Integer>>>
{
	private final int numBursts;
	private final Burst burst;
	
	public Explode(List<? extends Integer> matchList, ProbVector explodeOptions, int numTimes)
	{
		if (numTimes < 0)
			throw new IndexOutOfBoundsException();
		
		this.numBursts = numTimes;
		this.burst = new Burst(matchList, explodeOptions);
	}
	
	public Explode(Burst burst, int numTimes)
	{
		if (numTimes < 0)
			throw new IndexOutOfBoundsException();
		
		this.numBursts = numTimes;
		this.burst = burst;
	}
	
	@Override
	public DiceRollVector apply(List<Integer> li)
	{
		DiceRollVector drv = new DiceRollVector();
		drv.put(li, 1.0);
		
		for (int b = 0; b < numBursts; b++)
			drv = (DiceRollVector) drv.fork(burst);
		
		return drv;
	}

}
