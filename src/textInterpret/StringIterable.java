package textInterpret;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StringIterable implements Iterable<Character>
{
	private final String s;
	
	public StringIterable(String s)
	{
		this.s = s;
	}

	@Override
	public Iterator<Character> iterator()
	{
		return new StringIterator();
	}
	
	private class StringIterator implements Iterator<Character>
	{
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
				throw new NoSuchElementException();
			
			Character c = s.charAt(pos);
			pos++;
			return c;
		}
	}
}