package diceTools.function;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class DiscardN<T> implements Function<List<T>, List<T>>
{
	private final int nDiscard;
	private final Comparator<? super T> comp;
	
	public DiscardN(int nDiscard, Comparator<? super T> comp)
	{
		this.nDiscard = nDiscard;
		this.comp = comp;
	}
	
	@Override
	public List<T> apply(List<T> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		if (li.size() - nDiscard <= 0)
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
	
	public static <C extends Comparable<C>> Function<List<C>, List<C>> discardNaturalN(int nDiscard)
	{
		return new DiscardN<C>(nDiscard, Comparator.naturalOrder());
	}
	
	public static <C extends Comparable<C>> Function<List<C>, List<C>> discardReverseN(int nDiscard)
	{
		return new DiscardN<C>(nDiscard, Comparator.reverseOrder());
	}
	
	public static <N extends Number & Comparable<N>> Function<List<N>, List<N>> discardHighestN(int nDiscard)
	{
		return new DiscardN<N>(nDiscard, Comparator.naturalOrder());
	}
	
	public static <N extends Number & Comparable<N>> Function<List<N>, List<N>> discardLowestN(int nDiscard)
	{
		return new DiscardN<N>(nDiscard, Comparator.reverseOrder());
	}
}
