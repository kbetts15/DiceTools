package diceTools;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * <code>List</code> implementation which does not allow any modification
 * to the contents or structure of the list after construction.
 * 
 * @param <T> type of the elements stored in the <code>List</code>
 * 
 * @author kieran
 */
public class ImmutableList<T> implements List<T>
{
	/**
	 * Array storing the elements in the <code>ImmutableList</code>
	 */
	private final T[] myArray;
	
	/**
	 * Constructs an <code>ImmutableList</code> with elements copied in order from an array.
	 * @param ts	array from which elements are copied
	 */
	public ImmutableList(T[] ts)
	{
		myArray = Arrays.copyOf(ts, ts.length);
	}
	
	/**
	 * Constructs an <code>ImmutableList</code> by converting a <code>Collection</code>
	 * to an array, which is then stored.
	 * @param ts
	 */
	@SuppressWarnings("unchecked")
	public ImmutableList(Collection<? extends T> ts)
	{
		myArray = (T[]) ts.toArray();
	}

	@Override public boolean add(T e) {throw new UnsupportedOperationException();}
	@Override public void add(int index, T element) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(Collection<? extends T> c) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(int index, Collection<? extends T> c) {throw new UnsupportedOperationException();}
	@Override public void clear() {throw new UnsupportedOperationException();}
	@Override public boolean remove(Object o) {throw new UnsupportedOperationException();}
	@Override public T remove(int index) {throw new UnsupportedOperationException();}
	@Override public boolean removeAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public boolean retainAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public T set(int index, T element) {throw new UnsupportedOperationException();}

	@Override
	public boolean contains(Object o)
	{
		if (o == null)
		{
			for (T t : myArray)
				if (t == null)
					return true;
		}
		else
		{
			for (T t : myArray)
				if (t.equals(o))
					return true;
		}
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (Object o : c)
			if (this.contains(o))
				return true;
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		List<T> comp;
		
		try
		{
			comp = (List<T>) o;
		}
		catch (ClassCastException e)
		{
			return false;
		}
		
		Iterator<T> myIter = this.iterator();
		Iterator<T> compIter = comp.iterator();
		
		while (myIter.hasNext())
		{
			if (!compIter.hasNext())
				return false;
			
			T myNext = myIter.next();
			
			if (myNext == null ? compIter.next() != null : !myNext.equals(compIter.next()))
				return false;
		}
		
		return !compIter.hasNext();
	}

	@Override
	public T get(int index)
	{
		return myArray[index];
	}
	
	@Override
	public int hashCode()
	{
		int hash = 1;
		for (T t : myArray)
			hash = 31 * hash + (t == null ? 0 : t.hashCode());
		return hash;
	}

	@Override
	public int indexOf(Object o)
	{
		if (o == null)
		{
			for (int i = 0; i < myArray.length; i++)
				if (myArray[i] == null)
					return i;
		}
		else
		{
			for (int i = 0; i < myArray.length; i++)
				if (myArray[i].equals(o))
					return i;
		}
		
		return -1;
	}

	@Override
	public boolean isEmpty()
	{
		return myArray.length == 0;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new ImmutIterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		if (o == null)
		{
			for (int i = myArray.length - 1; i >= 0; i--)
				if (myArray[i] == null)
					return i;
		}
		else
		{
			for (int i = myArray.length - 1; i >= 0; i--)
				if (myArray[i].equals(o))
					return i;
		}
		
		return -1;
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return new ImmutListIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return new ImmutListIterator(index);
	}

	@Override
	public int size()
	{
		return myArray.length;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		T[] newArr = Arrays.copyOfRange(myArray, fromIndex, toIndex);
		return new ImmutableList<T>(newArr);
	}

	@Override
	public Object[] toArray()
	{
		return Arrays.copyOf(myArray, myArray.length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X[] toArray(X[] a)
	{
		X[] newArr;
		
		if (a.length >= myArray.length)
			newArr = a;
		else
			newArr = Arrays.copyOf(a, myArray.length);
		
		for (int i = 0; i < myArray.length; i++)
			newArr[i] = (X) myArray[i];
		
		if (newArr.length > myArray.length)
			newArr[myArray.length] = null;
		
		return newArr;
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer("[");
		
		ImmutIterator iter = new ImmutIterator();
		
		while (iter.hasNext())
		{
			s.append(iter.next().toString());
			
			if (iter.hasNext())
				s.append(", ");
		}
		
		s.append("]");
		
		return s.toString();
	}
	
	/**
	 * {@link Iterator} for elements in an {@link ImmutableList}.
	 * 
	 * @author kieran
	 */
	private class ImmutIterator implements Iterator<T>
	{
		/**
		 * Position of the next element to be returned by the <code>Iterator</code>
		 */
		private int nextPos = 0;
		
		@Override
		public boolean hasNext()
		{
			return nextPos < myArray.length;
		}

		@Override
		public T next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			
			return myArray[nextPos++];
		}
	}
	
	/**
	 * {@link ListIterator} for elements in an {@link ImmutableList}.
	 * 
	 * @author kieran
	 *
	 */
	private class ImmutListIterator implements ListIterator<T>
	{
		/**
		 * Current position of the <code>ListIterator</code>, ie the position in the <code>List</code>
		 * of the element which would be returned by a call to {@link ImmutListIterator#next() next()}
		 */
		private int currPos;
		
		/**
		 * Constructs a list-iterator of the elements in the list (in proper sequence),
		 * starting at the specified position in the list.
		 * 
		 * @param index		index of the first element to be returned from the list-iterator (by a call to next)
		 */
		public ImmutListIterator(int index)
		{
			currPos = index;
		}

		@Override public void add(T arg0) {throw new UnsupportedOperationException();}
		@Override public void remove() {throw new UnsupportedOperationException();}
		@Override public void set(T arg0) {throw new UnsupportedOperationException();}

		@Override
		public boolean hasNext()
		{
			return currPos < myArray.length;
		}

		@Override
		public boolean hasPrevious()
		{
			return currPos > 0;
		}

		@Override
		public T next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			
			return myArray[currPos++];
		}

		@Override
		public int nextIndex()
		{
			return currPos;
		}

		@Override
		public T previous()
		{
			if (!hasPrevious())
				throw new NoSuchElementException();
			
			return myArray[--currPos];
		}

		@Override
		public int previousIndex()
		{
			return currPos - 1;
		}
	}
}
