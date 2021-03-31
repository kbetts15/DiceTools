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
 * {@link diceTools.DicePoolMap#morph(Function) DicePoolMap(Function)}
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
	
	/**
	 * <code>Iterable</code> for single elements.
	 * Iterators produced by <code>IterSingle</code> produce a single
	 * element, exactly once if that element is non-null,
	 * zero times otherwise
	 *
	 * @param <X>	type of the element which is iterated
	 * 
	 * @author kieran
	 */
	private static class IterSingle<X> implements Iterable<X>
	{
		/**
		 * Element produced upon iteration
		 */
		private final X x;
		
		/**
		 * Constructs an <code>IterSingle<code> for an element
		 * @param x		element which the <code>IterSingle</code> produces
		 */
		public IterSingle(X x)
		{
			this.x = x;
		}
		
		@Override
		public Iterator<X> iterator()
		{
			return new It();
		}
		
		/**
		 * <code>Iterator</code> which produces the element stored by the
		 * <code>IterSingle</code> exactly once if the element is non-null,
		 * zero times otherwise.
		 * 
		 * @author kieran
		 */
		private class It implements Iterator<X>
		{
			/**
			 * Stores whether <code>next</code> has been called yet
			 */
			boolean unused;
			
			/**
			 * Constructs an <code>It</code> which produces the element
			 * stored by the <code>IterSingle</code>
			 */
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
