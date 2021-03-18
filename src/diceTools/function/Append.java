package diceTools.function;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.ImmutableList;

/**
 * {@link java.util.function.Function#Function Function} to append values
 * to a <code>List</code>. This process is performed by copying the
 * <code>List</code>, then appending the new values to the copy,
 * which is returned.
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
	 */
	private final Iterable<T> it;
	
	/**
	 * Constructs an <code>Append</code> which appends the element
	 * <code>t</code> to a <code>List</code>
	 * @param t		element to append
	 **/
	public Append(T t)
	{
		it = new IterSingle<T>(t);
	}
	
	/**
	 * Constructs an <code>Append</code> which appends the elements
	 * in a <code>Collection<code> (in order) to a <code>List</code>
	 * @param c		<code>Collection</code> containing the elements
					to be appended
	 **/
	public Append(Collection<? extends T> c)
	{
		it = new ImmutableList<T>(c);
	}
	
	/**
	 * Constructs an <code>Append<code> which appends all elements generated
	 * by an <code>Iterable</code> (in order) to a <code>List</code>
	 * @param it	<code>Iterable</code> which generates the elements
	 * 				to be appended
	 */
	public Append(Iterable<T> it)
	{
		this.it = it;
	}

	@Override
	public List<T> apply(List<T> li)
	{
		List<T> liNew = new LinkedList<T>(li);
		for (T t : it)
			liNew.add(t);
		return liNew;
	}
	
	private static class IterSingle<X> implements Iterable<X>
	{
		private final X x;
		
		public IterSingle(X x)
		{
			this.x = x;
		}
		
		@Override
		public Iterator<X> iterator()
		{
			return new It();
		}
		
		private class It implements Iterator<X>
		{
			boolean unused;
			
			public It()
			{
				unused = (x != null);
			}

			@Override
			public boolean hasNext()
			{
				return unused;
			}

			@Override
			public X next()
			{
				unused = false;
				return x;
			}
		}
	}
}
