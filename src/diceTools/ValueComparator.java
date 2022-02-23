package diceTools;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class ValueComparator <T> implements Comparator<Map.Entry<?, ? extends T>>
{
	private final Comparator<? super T> valueComparator;
	
	public static <X extends Comparable<? super X>> ValueComparator<X> naturalOrder()
	{
		return new ValueComparator<X>(Comparator.naturalOrder());
	}
	
	public ValueComparator(Comparator<? super T> valueComparator)
	{
		this.valueComparator = valueComparator;
	}
	
	public Comparator<? super T> getKeyComparator()
	{
		return valueComparator;
	}
	
	@Override
	public int compare(Entry<?, ? extends T> o1, Entry<?, ? extends T> o2)
	{
		return valueComparator.compare(o1.getValue(), o2.getValue());
	}
}
