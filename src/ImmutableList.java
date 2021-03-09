import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ImmutableList<T> implements List<T>
{
	private final List<T> myList;
	
	public ImmutableList(Collection<? extends T> c)
	{
		myList = new LinkedList<T>(c);
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>() {

			Iterator<T> iter = myList.iterator();
			
			@Override public boolean hasNext() {return iter.hasNext();}
			@Override public T next() {return iter.next();}
			
		};
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return new ListIteratorWrapper<T>(myList.listIterator());
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return new ListIteratorWrapper<T>(myList.listIterator(index));
	}
	
	private static class ListIteratorWrapper<T> implements ListIterator<T>
	{
		private final ListIterator<T> li;
		
		public ListIteratorWrapper(ListIterator<T> li)
		{
			this.li = li;
		}
		
		//Unsupported
		@Override public void add(T arg0) {throw new UnsupportedOperationException();}
		@Override public void remove() {throw new UnsupportedOperationException();}
		@Override public void set(T arg0) {throw new UnsupportedOperationException();}

		//Referred to li
		@Override public boolean hasNext() {return li.hasNext();}
		@Override public boolean hasPrevious() {return li.hasPrevious();}
		@Override public T next() {return li.next();}
		@Override public int nextIndex() {return li.nextIndex();}
		@Override public T previous() {return li.previous();}
		@Override public int previousIndex() {return li.previousIndex();}
	}

	//Unsupported abstract
	@Override public boolean add(T arg0) {throw new UnsupportedOperationException();}
	@Override public void add(int arg0, T arg1) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(Collection<? extends T> arg0) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(int arg0, Collection<? extends T> arg1) {throw new UnsupportedOperationException();}
	@Override public void clear() {throw new UnsupportedOperationException();}
	@Override public boolean remove(Object arg0) {throw new UnsupportedOperationException();}
	@Override public T remove(int arg0) {throw new UnsupportedOperationException();}
	@Override public boolean removeAll(Collection<?> arg0) {throw new UnsupportedOperationException();}
	@Override public boolean retainAll(Collection<?> arg0) {throw new UnsupportedOperationException();}
	@Override public T set(int arg0, T arg1) {throw new UnsupportedOperationException();}
	
	//Unsupported inherited
	@Override public boolean removeIf(Predicate<? super T> filter) {throw new UnsupportedOperationException();}
	@Override public void replaceAll(UnaryOperator<T> operator) {throw new UnsupportedOperationException();}
	@Override public void sort(Comparator<? super T> c) {throw new UnsupportedOperationException();}
	@Override public Spliterator<T> spliterator() {throw new UnsupportedOperationException();}

	//Referred to myList
	@Override public boolean contains(Object o) {return myList.contains(o);}
	@Override public boolean containsAll(Collection<?> c){return myList.containsAll(c);}
	@Override public T get(int index) {return myList.get(index);}
	@Override public int indexOf(Object o) {return myList.indexOf(o);}
	@Override public boolean isEmpty() {return myList.isEmpty();}
	@Override public int lastIndexOf(Object o) {return myList.lastIndexOf(o);}
	@Override public int size() {return myList.size();}
	@Override public List<T> subList(int fromIndex, int toIndex) {return myList.subList(fromIndex, toIndex);}
	@Override public Object[] toArray() {return myList.toArray();}
	@Override public <X> X[] toArray(X[] arr) {return myList.toArray(arr);}
}