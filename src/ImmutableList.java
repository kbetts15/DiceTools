import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ImmutableList<T> implements List<T>
{
	private final T[] myArray;
	
	public ImmutableList(T[] ts)
	{
		myArray = Arrays.copyOf(ts, ts.length);
	}
	
	@SuppressWarnings("unchecked")
	public ImmutableList(Collection<T> ts)
	{
		myArray = (T[]) ts.toArray();
	}

	@Override public boolean add(T e) {throw new UnsupportedOperationException();}
	@Override public void add(int index, T element) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(Collection<? extends T> c) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(int index, Collection<? extends T> c) {throw new UnsupportedOperationException();}
	@Override public void clear() {throw new UnsupportedOperationException();}

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
			
			if (myNext == null && compIter.next() != null)
				return false;
			
			if (!myNext.equals(compIter.next()))
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

	@Override public boolean remove(Object o) {throw new UnsupportedOperationException();}
	@Override public T remove(int index) {throw new UnsupportedOperationException();}
	@Override public boolean removeAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public boolean retainAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public T set(int index, T element) {throw new UnsupportedOperationException();}

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
	
	private class ImmutIterator implements Iterator<T>
	{
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
	
	private class ImmutListIterator implements ListIterator<T>
	{
		private int currPos;
		
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
