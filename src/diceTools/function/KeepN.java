package diceTools.function;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.ImmutableList;

public class KeepN<T> implements Function<List<T>, List<T>>
{
	private final int nKeep;
	private final Comparator<? super T> comp;
	
	public KeepN(int nKeep, Comparator<? super T> comp)
	{
		this.nKeep = nKeep;
		this.comp = comp;
	}
	
	@Override
	public List<T> apply(List<T> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		if (li.size() <= nKeep)
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

	public static <C extends Comparable<C>> Function<List<C>, List<C>> keepNaturalN(int nKeep)
	{
		return new KeepN<C>(nKeep, Comparator.naturalOrder());
	}
	
	public static <C extends Comparable<C>> Function<List<C>, List<C>> keepReverseN(int nKeep)
	{
		return new KeepN<C>(nKeep, Comparator.reverseOrder());
	}
	
	public static <N extends Number & Comparable<N>> Function<List<N>, List<N>> keepHighestN(int nKeep)
	{
		return new KeepN<N>(nKeep, Comparator.naturalOrder());
	}
	
	public static <N extends Number & Comparable<N>> Function<List<N>, List<N>> keepLowestN(int nKeep)
	{
		return new KeepN<N>(nKeep, Comparator.reverseOrder());
	}
}
