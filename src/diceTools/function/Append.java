package diceTools.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.ImmutableList;

public class Append <T> implements Function<List<T>, List<T>>
{
	private final Collection<T> app;
	
	public Append(T t)
	{
		ArrayList<T> arrL = new ArrayList<T>(1);
		arrL.add(t);
		app = new ImmutableList<T>(arrL);
	}
	
	public Append(Collection<? extends T> c)
	{
		app = new ImmutableList<T>(c);
	}

	@Override
	public List<T> apply(List<T> li)
	{
		List<T> liNew = new LinkedList<T>(li);
		liNew.addAll(app);
		return liNew;
	}
}
