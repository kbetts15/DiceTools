package diceTools.function;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.ImmutableList;
import diceTools.DiceRollMap;

/**
 * {@link java.util.function.Function Function} to replace values in a
 * <code>List</code> with each value from a {@link DiceRollMap} to generate a {@link DicePoolMap}
 * 
 * @author kieran
 */
public class Burst implements Function<List<? extends DiceNumber>, DicePoolMap>
{
	/**
	 * <code>Function</code> representing the rule for replacing <code>DiceNumber</code>s.
	 * If no replacement should be made for a given <code>DiceNumber</code>,
	 * {@link Function#apply(Object) apply} shall return <code>null</code>.
	 */
	private final Function<? super DiceNumber, ? extends DiceRollMap> replacer;
	
	/**
	 * Constructs a <code>Burst</code> with a given <code>List</code>
	 * of {@link DiceNumber}s to match and a given
	 * {@link DiceRollMap} of replacements
	 * 
	 * @param matchList			<code>List</code> of <code>DiceNumber</code>s to be replaced
	 * @param explodeOptions	<code>DiceRollMap</code> of replacements and their probabilities
	 */
	public Burst(List<? extends DiceNumber> matchList, DiceRollMap explodeOptions)
	{
		replacer = new Function<DiceNumber, DiceRollMap>() {
			
			private final ImmutableList<DiceNumber> match = new ImmutableList<DiceNumber>(matchList);
			private final DiceRollMap explode = new DiceRollMap(explodeOptions);
			
			@Override
			public DiceRollMap apply(DiceNumber n)
			{
				if (match.contains(n))
					return explode;
				else
					return null;
			}
		};
		
	}
	
	/**
	 * Constructs a <code>Burst</code> with a given <code>List</code>
	 * of {@link DiceNumber}s to match and replace with the values
	 * rolled on a die with a given number of sides.
	 * 
	 * @param matchList		<code>List</code> of <code>DiceNumber</code>s to be replaced
	 * @param numSides		number of sides on the die to determine replacements
	 */
	public Burst(List<? extends DiceNumber> matchList, int numSides)
	{
		this(matchList, DiceRollMap.diceRoll(1, numSides));
	}
	
	/**
	 * Constructs a <code>Burst</code> with a given rule for replacing
	 * {@link DiceNumber}s with replacements and their probabilities.
	 * 
	 * @param replacer	<code>Function</code> representing the rule for replacing <code>DiceNumber</code>s.
	 * 					If no replacement should be made for a given <code>DiceNumber</code>,
	 * 					{@link Function#apply(Object) apply} shall return <code>null</code>.
	 */
	public Burst(Function<? super DiceNumber, ? extends DiceRollMap> replacer)
	{
		this.replacer = replacer;
	}

	@Override
	public DicePoolMap apply(List<? extends DiceNumber> li)
	{
		if (li == null)
			throw new NullPointerException("Input List is null");
		
		DicePoolMap dpm = new DicePoolMap();
		
		if (li.isEmpty())
			return dpm;
		
		DiceNumber checkN = li.get(0);
		DiceRollMap repMap = replacer.apply(checkN);
		
		if (repMap == null)
		{
			List<DiceNumber> key = new LinkedList<DiceNumber>();
			key.add(checkN);
			dpm.put(key, 1.0);
		}
		else
			dpm = new DicePoolMap(repMap);
		
		for (int checkPos = 1; checkPos < li.size(); checkPos++)
		{
			checkN = li.get(checkPos);
			repMap = replacer.apply(checkN);
			
			if (repMap == null)
			{
				Append<DiceNumber> app = new Append<DiceNumber>(checkN);
				dpm = (DicePoolMap) dpm.morph(app);
			}
			else
				dpm = dpm.combine(repMap);
		}
		
		return dpm;
	}

}
