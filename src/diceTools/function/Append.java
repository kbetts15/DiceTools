package diceTools.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.ImmutableList;

/**
 * {@link java.util.function.Function#Function Function} to append values
 * to a <code>List</code>. This process is performed by copying the
 * <code>List</code>, then appending the new values.
 *
 * <p><code>Append&ltInteger&gt</code> is a valid argument for
 * {@link diceTools.DiceRollVector#morph(Function) DiceRollVector(Function)}
 *
 * <T> type of the elements in the <code>List</code>
 *
 * @author kieran
 **/
public class Append <T> implements Function<List<T>, List<T>>
{
	/**
	 * Elements to append to each <code>List</code>
	 **/
	private final Collection<T> app;
	
	/**
	 * Constructs an <code>Append</code> which appends the element
	 * <code>t</code> to a <code>List</code>
	 * @param t		element to append
	 **/
	public Append(T t)
	{
		ArrayList<T> arrL = new ArrayList<T>(1);
		arrL.add(t);
		app = new ImmutableList<T>(arrL);
	}
	
	/**
	 * Constructs an <code>Append</code> which appends the elements
	 * in a <code>Collection<code> (in order) to a <code>List</code>
	 * @param c		<code>Collection</code> containing the elements
					to be appended
	 **/
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
