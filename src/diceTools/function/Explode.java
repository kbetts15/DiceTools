package diceTools.function;

import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;

/**
 * {@link java.util.function.Function#Function Function} to repeatedly apply
 * <code>Burst.{@link Burst#apply(List) apply}(List)</code> to a
 * <code>List&lt{@link DiceNumber}&gt</code>
 * 
 * @see Burst
 * 
 * @author kieran
 */
public class Explode implements Function<List<? extends DiceNumber>, DicePoolMap>
{
	/**
	 * <code>Burst</code> to repeatedly apply to a <code>List</code>
	 */
	private final Burst burst;
	
	/**
	 * Number of times to apply a <code>Burst</code> to a <code>List</code>
	 */
	private final int numBursts;
	
	/**
	 * Constructs an <code>Explode</code> with a specified <code>Burst</code>
	 * and number of times to apply it
	 * 
	 * @param burst		<code>Burst</code> to repeatedly apply to <code>List</code>s
	 * @param numTimes	Number of times to apply the <code>Burst</code>
	 */
	public Explode(Burst burst, int numTimes)
	{
		if (numTimes < 0)
			throw new IndexOutOfBoundsException();
		
		this.numBursts = numTimes;
		this.burst = burst;
	}
	
	@Override
	public DicePoolMap apply(List<? extends DiceNumber> li)
	{
		DicePoolMap dpm = new DicePoolMap();
		dpm.put(li, 1.0);
		
		for (int b = 0; b < numBursts; b++)
			dpm = (DicePoolMap) dpm.fork(burst);
		
		return dpm;
	}

}
