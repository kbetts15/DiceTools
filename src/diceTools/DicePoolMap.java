package diceTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementation of {@link ProbMap} with <code>List&lt{@link DiceNumber}&gt</code> values used for event keys.
 * 
 * <p>Event keys are never permitted to be <code>null</code>,
 * nor are they permitted to contain <code>null</code> values.
 * The ordering of <code>List</code> keys is not preserved,
 * as they are sorted before accessing the underlying <code>HashMap</code>.
 * 
 * @author kieran
 */
@SuppressWarnings("serial")
public class DicePoolMap extends ProbMap<List<? extends DiceNumber>>
{
	
	/**
	 * Sums each <code>Integer</code> in a <code>List&ltInteger&gt</code> key
	 */
	private static final Function<List<? extends DiceNumber>, ? extends DiceNumber> sumFlatten;
	
	/**
	 * Copies a <code>List</code> of <code>Integer</code>s
	 * and <code>add</code>s an <code>Integer</code> to the copy
	 */
	private static final BiFunction<List<? extends DiceNumber>, DiceNumber, List<DiceNumber>> listAdd;
	
	/**
	 * Copies a <code>List</code> of <code>Integer</code>s
	 * and <code>add</code>s all <code>Integer</code>s
	 * from another <code>List</code> to the copy.
	 */
	private static final BiFunction<List<? extends DiceNumber>, List<? extends DiceNumber>, List<DiceNumber>> listAddAll;
	
	private static final Comparator<List<? extends DiceNumber>> listCompare;
	
	static
	{
		sumFlatten = new Function<List<? extends DiceNumber>, DiceNumber>()
		{
			@Override
			public DiceNumber apply(List<? extends DiceNumber> key)
			{
				boolean isDouble = false;
				int intTotal = 0;
				double doubleTotal = 0.0;
				
				for (DiceNumber myNum : key)
				{
					if (!isDouble && !myNum.isInt())
					{
						isDouble = true;
						doubleTotal = intTotal;
					}
					
					if (isDouble)
						doubleTotal += myNum.doubleValue();
					else
						intTotal += myNum.intValue();
				}
				
				if (isDouble)
					return new DiceNumber.DiceDouble(doubleTotal);
				else
					return new DiceNumber.DiceInteger(intTotal);
			}
		};
		
		listAdd = new BiFunction<List<? extends DiceNumber>, DiceNumber, List<DiceNumber>>()
		{
			@Override
			public List<DiceNumber> apply(List<? extends DiceNumber> t, DiceNumber u)
			{
				List<DiceNumber> newList = new LinkedList<DiceNumber>(t);
				newList.add(u);
				return newList;
			}
		};
		
		listAddAll = new BiFunction<List<? extends DiceNumber>, List<? extends DiceNumber>, List<DiceNumber>>()
		{
			@Override
			public List<DiceNumber> apply(List<? extends DiceNumber> t, List<? extends DiceNumber> u)
			{
				List<DiceNumber> newList = new LinkedList<DiceNumber>(t);
				newList.addAll(u);
				return newList;
			}
		};
		
		listCompare = new Comparator<List<? extends DiceNumber>>()
		{

			@Override
			public int compare(List<? extends DiceNumber> listA, List<? extends DiceNumber> listB)
			{
				final int sizeA = listA.size();
				final int sizeB = listB.size();
				
				if (sizeA != sizeB)
					return Integer.compare(sizeA, sizeB);
				
				Iterator<? extends DiceNumber> iterA = listA.iterator();
				Iterator<? extends DiceNumber> iterB = listB.iterator();
				
				while (iterA.hasNext())
				{
					DiceNumber nA = iterA.next();
					DiceNumber nB = iterB.next();
					
					if (!nA.equals(nB))
						return nA.compareTo(nB);
				}
				
				return 0;
			}
			
		};
	}
	
	public DicePoolMap()
	{
		super(listCompare);
	}
	
	public DicePoolMap(DicePoolMap dpm)
	{
		super(dpm);
	}
	
	public DicePoolMap(DiceRollMap drm)
	{
		super();
		for (Entry<DiceNumber, Double> drmEntry : drm.entrySet())
		{
			List<DiceNumber> drmInt = new ArrayList<DiceNumber>(1);
			drmInt.add(drmEntry.getKey());
			this.put(drmInt, drmEntry.getValue());
		}
	}
	
	@Override
	public boolean keyIsValid(List<? extends DiceNumber> key)
	{
		if (key == null)
			return false;
		
		for (DiceNumber keyInt : key)
			if (keyInt == null)
				return false;
		
		return true;
	}
	
	@Override
	public List<DiceNumber> sanitizeKey(List<? extends DiceNumber> key)
	{
		List<DiceNumber> sanitizedKey = new LinkedList<DiceNumber>(key);
		Collections.sort(sanitizedKey);
		sanitizedKey = new ImmutableList<DiceNumber>(sanitizedKey);
		return sanitizedKey;
	}
	
	@Override
	public List<DiceNumber> makeKey(Object oKey)
	{
		if (oKey instanceof DiceNumber[])
		{
			DiceNumber[] keyArr = (DiceNumber[]) oKey;
			return Arrays.asList(keyArr);
		}
		
		if (oKey instanceof Integer[])
		{
			Integer[] intArr = (Integer[]) oKey;
			DiceNumber[] keyArr = new DiceNumber[intArr.length];
			
			for (int i = 0; i < intArr.length; i++)
				keyArr[i] = new DiceNumber.DiceInteger(intArr[i]);
			
			return Arrays.asList(keyArr);
		}
		
		if (oKey instanceof Double[])
		{
			Double[] doubleArr = (Double[]) oKey;
			DiceNumber[] keyArr = new DiceNumber[doubleArr.length];
			
			for (int i = 0; i < doubleArr.length; i++)
				keyArr[i] = new DiceNumber.DiceDouble(doubleArr[i]);
			
			return Arrays.asList(keyArr);
		}
		
		@SuppressWarnings("unchecked")
		List<? extends DiceNumber> key = (List<? extends DiceNumber>) oKey;
		return new LinkedList<DiceNumber>(key);
	}
	
	@Override
	public DicePoolMap get()
	{
		return new DicePoolMap();
	}
	
	/**
	 * Generate the {@link DicePoolMap} which results from rolling a set of identical unbiased dice 
	 * @param numDice	number of dice to be rolled
	 * @param sides		number of sides on the dice
	 * @return			probabilities of all possible outcomes
	 */
	public static DicePoolMap diceRoll(int numDice, int sides)
	{
		DiceRollIterable dri = new DiceRollIterable(numDice, sides);
		
		DicePoolMap dpm = new DicePoolMap();
		dpm.putAll(dri);

		return dpm;
	}
	
	/**
	 * Generate all the possible results of combining each roll array from the calling
	 * {@link DicePoolMap} with each possible roll from the given {@link DiceRollMap}.
	 * @param drm	<code>DiceRollMap</code> to be combined
	 * @return		resulting <code>DicePoolMap</code>
	 */
	public DicePoolMap combine(DiceRollMap drm)
	{
		if (this.isEmpty())
		{
			DicePoolMap dpmNew = new DicePoolMap();
			
			for (Entry<? extends DiceNumber, Double> entry : drm.entrySet())
			{
				List<DiceNumber> listNew = new LinkedList<DiceNumber>();
				listNew.add(entry.getKey());
				dpmNew.put(listNew, entry.getValue());
			}
			
			return dpmNew;
		}
		
		return (DicePoolMap) combine(listAdd, drm, this);
	}
	
	/**
	 * Generate all the possible results of combining each roll array from the calling
	 * {@link DicePoolMap} with each roll array from the given <code>DicePoolMap</code>.
	 * @param dpm	<code>DicePoolMap</code> to be combined
	 * @return		resulting <code>DicePoolMap</code>
	 */
	public DicePoolMap combine(DicePoolMap dpm)
	{
		if (this.isEmpty())
		{
			DicePoolMap dpmNew = new DicePoolMap();
			dpmNew.putAll(dpm);
			return dpmNew;
		}
		
		return (DicePoolMap) combine(listAddAll, dpm, this);
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @param function	function which combines the rolls
	 * @return			{@link DiceRollMap} storing the combined rolls and their probabilities
	 */
	public DiceRollMap flatten(Function<? super List<? extends DiceNumber>, ? extends DiceNumber> function)
	{
		return (DiceRollMap) super.morph(function, new DiceRollMap().supplyMe());
	}
	
	/**
	 * Combine the rolls in each roll array to a single Integer
	 * @return			{@link DiceRollMap} storing the summed rolls and their probabilities
	 */
	public DiceRollMap flatten()
	{
		return flatten(sumFlatten);
	}
}
