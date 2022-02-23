package diceTools;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class KeyComparator <T> implements Comparator<Map.Entry<? extends T, ?>>
{
	private final Comparator<? super T> keyComparator;
	
	public static <X extends Comparable<? super X>> KeyComparator<X> naturalOrder()
	{
		return new KeyComparator<X>(Comparator.naturalOrder());
	}
	
	public KeyComparator(Comparator<? super T> keyComparator)
	{
		this.keyComparator = keyComparator;
	}
	
	public Comparator<? super T> getKeyComparator()
	{
		return keyComparator;
	}
	
	@Override
	public int compare(Entry<? extends T, ?> o1, Entry<? extends T, ?> o2)
	{
		return keyComparator.compare(o1.getKey(), o2.getKey());
	}
}