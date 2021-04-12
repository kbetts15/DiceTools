package textInterpret;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides an <code>Iterable</code> view of a <code>String</code> with which to iterate characters
 * 
 * @author kieran
 */
public class StringIterable implements Iterable<Character>
{
	/**
	 * <code>String</code> for which the <code>StringIterable</code> provides character iterators
	 */
	private final String s;
	
	/**
	 * Instantiate a {@link StringIterable}
	 * @param s		<code>String</code> for which the <code>StringIterable</code> will provide character iterators
	 */
	public StringIterable(String s)
	{
		this.s = s;
	}

	@Override
	public Iterator<Character> iterator()
	{
		return new StringIterator();
	}
	
	/**
	 * Iterates the characters of a <code>String</code>
	 * 
	 * @author kieran
	 */
	private class StringIterator implements Iterator<Character>
	{
		/**
		 * Current position in the <code>String</code>.
		 * This is equivalent to the position of the next
		 * <code>Character</code> to be returned by
		 * {@link StringIterator#next next()}
		 */
		private int pos = 0;
		
		@Override
		public boolean hasNext()
		{
			return pos < s.length();
		}

		@Override
		public Character next()
		{
			if (!hasNext())
				throw new NoSuchElementException("No more characters to get");
			
			Character c = s.charAt(pos);
			pos++;
			return c;
		}
	}
}