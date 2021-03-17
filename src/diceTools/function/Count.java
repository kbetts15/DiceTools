package diceTools.function;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Count implements Function<List<Integer>, Integer>
{
	private final Predicate<Integer> pred;
	
	public Count(Predicate<Integer> pred)
	{
		this.pred = pred;
	}
	
	@Override
	public Integer apply(List<Integer> li)
	{
		if (li == null)
			throw new NullPointerException();
		
		int count = 0;
		
		for (Integer checkInt : li)
			if (pred.test(checkInt))
				count++;
		
		return count;
	}

	public static Count countVal(int val)
	{
		Predicate<Integer> p = (x) -> {return x.equals(val);};
		return new Count(p);
	}
}
