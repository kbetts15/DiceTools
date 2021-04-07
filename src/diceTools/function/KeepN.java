package diceTools.function;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.ImmutableList;

/**
 * {@link java.util.function.Function#Function Function} to keep some values
 * in a <code>List</code>, and discard the rest. This process is performed by
 * copying the first <code>nKeep<code> elements of the <code>List</code>
 * (according to a {@link java.util.Comparator#Comparator Comparator})
 * to a new <code>List</code>, which is returned.
 * 
 * <p><code>KeepN&ltInteger&gt</code> is a valid argument for
 * {@link diceTools.DicePoolMap#morph(Function) DicePoolMap.morph(Function)}
 * 
 * @param <T> type of the elements in the <code>List</code>
 * 
 * @author kieran
 */
public class KeepN<T> implements Function<List<? extends T>, List<T>>
{
	/**
	 * DiceNumber of elements to keep
	 */
	private final int nKeep;
	
	/**
	 * {@link java.util.Comparator#Comparator Comparator}
	 * defining an order for elements of type <code>T</code>
	 */
	private final Comparator<? super T> comp;
	
	/**
	 * Constructs a <code>KeepN</code> which keeps the first <code>nKeep</code>
	 * elements of a <code>List</code> according to the ordering defined by
	 * a {@link java.util.Comparator#Comparator Comparator}
	 * @param nKeep		DiceNumber of elements to keep
	 * @param comp		<code>Comparator</code> defining an ordering of elements
	 */
	public KeepN(int nKeep, Comparator<? super T> comp)
	{
		this.nKeep = nKeep;
		this.comp = comp;
	}
	
	@Override
	public List<T> apply(List<? extends T> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		final int size = li.size();
		
		if (size <= nKeep)
			return new ImmutableList<T>(li);
		
		List<T> liCopy = new ArrayList<T>(li);
		List<T> liKeep = new LinkedList<T>();
		
		for (int pick = 0; pick < nKeep; pick++)
		{
			T pickT = null;
			int pickPos = -1;
			
			for (int checkPos = 0; checkPos < liCopy.size(); checkPos++)
			{
				final T checkT = liCopy.get(checkPos);
				
				if ((pickT == null) || (comp.compare(checkT, pickT) > 0))
				{
					pickT = checkT;
					pickPos = checkPos;
				}
			}
			
			liKeep.add(pickT);
			liCopy.remove(pickPos);
		}
		
		return liKeep;
	}

	/**
	 * Creates a <code>KeepN</code> which orders elements by their natural ordering
	 * @param nKeep		DiceNumber of elements to keep
	 * @return			<code>KeepN</code> which keeps the first <code>nKeep</code> elements of
	 * 					a <code>List</code> according to the elements' natural ordering
	 */
	public static <C extends Comparable<? super C>> Function<List<? extends C>, List<C>> keepNaturalN(int nKeep)
	{
		return new KeepN<C>(nKeep, Comparator.naturalOrder());
	}
	
	/**
	 * Creates a <code>KeepN</code> which orders elements by their reversed natural ordering
	 * @param nKeep		DiceNumber of elements to keep
	 * @return			<code>KeepN<code> which keeps the last <code>nKeep</code> elements of
	 * 					a <code>List</code> according to the elements' natural ordering
	 */
	public static <C extends Comparable<? super C>> Function<List<? extends C>, List<C>> keepReverseN(int nKeep)
	{
		return new KeepN<C>(nKeep, Comparator.reverseOrder());
	}
	
	/**
	 * Creates a <code>KeepN</code> which orders <code>DiceNumber<code>'s in descending order
	 * @param nKeep		DiceNumber of <code>DiceNumber</code>'s to keep
	 * @return			<code>KeepN</code> which keeps the highest <code>nKeep</code> elements
	 */
	public static <N extends DiceNumber> Function<List<? extends N>, List<N>> keepHighestN(int nKeep)
	{
		return new KeepN<N>(nKeep, Comparator.naturalOrder());
	}
	
	/**
	 * Creates a <code>KeepN</code> which orders <code>DiceNumber<code>'s in ascending order
	 * @param nKeep		DiceNumber of <code>DiceNumber</code>'s to keep
	 * @return			<code>KeepN</code> which keeps the lowest <code>nKeep</code> elements
	 */
	public static <N extends DiceNumber> Function<List<? extends N>, List<N>> keepLowestN(int nKeep)
	{
		return new KeepN<N>(nKeep, Comparator.reverseOrder());
	}
}
