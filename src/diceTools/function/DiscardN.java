package diceTools.function;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;

/**
 * {@link java.util.function.Function Function} to discard some values
 * in a <code>List</code>. This process is performed by copying all but the first
 * <code>nDiscard</code> elements of the <code>List</code> (according to a
 * {@link java.util.Comparator Comparator}) to a new <code>List</code>,
 * which is returned.
 * 
 * <p><code>DiscardN&lt;Integer&gt;</code> is a valid argument for
 * {@link diceTools.DicePoolMap#morph(Function) DicePoolMap.morph(Function)}
 * 
 * @param <T> type of the elements in the <code>List</code>
 * 
 * @author kieran
 */
public class DiscardN<T> implements Function<List<? extends T>, List<T>>
{
	/**
	 * DiceNumber of elements to discard
	 */
	private final int nDiscard;
	
	/**
	 * {@link java.util.Comparator#Comparator Comparator}
	 * defining an order for elements of type <code>T</code>
	 */
	private final Comparator<? super T> comp;
	
	/**
	 * Constructs a <code>DiscardN</code> which discards the first
	 * {@code nDiscard} elements of a <code>List</code>
	 * according to the ordering defined by a
	 * {@link java.util.Comparator Comparator}
	 * @param nDiscard		DiceNumber of elements to discard
	 * @param comp			<code>Comparator</code> defining an ordering
	 * 						of elements
	 */
	public DiscardN(int nDiscard, Comparator<? super T> comp)
	{
		this.nDiscard = nDiscard;
		this.comp = comp;
	}
	
	@Override
	public List<T> apply(List<? extends T> li)
	{
		if (li == null)
			throw new NullPointerException("Input List is null");
		
		final int size = li.size();
		
		if (size - nDiscard <= 0)
			return new ArrayList<T>(0);
		
		List<T> liRemain = new ArrayList<T>(li);
		
		for (int disc = 0; disc < nDiscard; disc++)
		{
			T discT = null;
			int discPos = -1;
			
			for (int checkPos = 0; checkPos < liRemain.size(); checkPos++)
			{
				final T checkT = liRemain.get(checkPos);
				
				if ((discT == null) || (comp.compare(checkT, discT) > 0))
				{
					discT = checkT;
					discPos = checkPos;
				}
			}
			
			liRemain.remove(discPos);
		}
		
		return liRemain;
	}
	
	/**
	 * Creates a <code>DiscardN</code> which orders elements by their natural ordering
	 * @param <C>			{@link Comparable} capable of comparing itself
	 * @param nDiscard		DiceNumber of elements to discard
	 * @return				<code>KeepN</code> which keeps the first <code>nDiscard</code>
	 * 						elements of a <code>List</code> according to the elements'
	 * 						natural ordering
	 */
	public static <C extends Comparable<? super C>> Function<List<? extends C>, List<C>> discardNaturalN(int nDiscard)
	{
		return new DiscardN<C>(nDiscard, Comparator.naturalOrder());
	}
	
	/**
	 * Creates a <code>DiscardN</code> which orders elements by their reversed natural ordering
	 * @param <C>			{@link Comparable} capable of comparing itself
	 * @param nDiscard		DiceNumber of elements to discard
	 * @return				{@code DiscardN} which discards the last {@code nDiscard}
	 * 						elements of a <code>List</code> according to elements'
	 * 						natural ordering
	 */
	public static <C extends Comparable<? super C>> Function<List<? extends C>, List<C>> discardReverseN(int nDiscard)
	{
		return new DiscardN<C>(nDiscard, Comparator.reverseOrder());
	}
	
	/**
	 * Creates a <code>DiscardN</code> which orders {@code DiceNumber}'s in descending order
	 * @param nDiscard		DiceNumber of <code>DiceNumber</code>'s to keep
	 * @return				<code>DiscardN</code> which discards the highest <code>nDiscard</code> elements
	 */
	public static Function<List<? extends DiceNumber>, List<DiceNumber>> discardHighestN(int nDiscard)
	{
		return new DiscardN<DiceNumber>(nDiscard, Comparator.naturalOrder());
	}
	
	/**
	 * Creates a <code>DiscardN</code> which orders {@code DiceNumber}'s in ascending order
	 * @param nDiscard		DiceNumber of <code>DiceNumber</code>'s to keep
	 * @return				<code>DiscardN</code> which discards the lowest <code>nDiscard</code> elements
	 */
	public static Function<List<? extends DiceNumber>, List<DiceNumber>> discardLowestN(int nDiscard)
	{
		return new DiscardN<DiceNumber>(nDiscard, Comparator.reverseOrder());
	}
}
