import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DiceRollVector extends HashMap<Integer[], Double>
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public Double put(Integer[] key, Double value)
	{
		if (key == null || value == null)
			return null;
		
		for (Integer keyInt : key)
			if (keyInt == null)
				return null;
		
		Arrays.sort(key);
		
		return super.put(key, value);
	}
	
	@Override
	public Double get(Object key)
	{
		if (key == null || !(key instanceof Integer[]))
			return null;
		
		Integer[] keyArr = (Integer[]) key;
		
		for (Integer keyInt : keyArr)
			if (keyInt == null)
				return null;
		
		Arrays.sort(keyArr);

		return super.get(keyArr);
	}
	
	@Override
	public void putAll(Map<? extends Integer[], ? extends Double> map)
	{
		map.forEach(putter);
	}
	
	@Override
	public Double putIfAbsent(Integer[] key, Double value)
	{
		if (key == null || value == null)
			return null;
		
		for (Integer keyInt : key)
			if (keyInt == null)
				return null;
		
		Arrays.sort(key);
		
		return super.putIfAbsent(key, value);
	}
	
	@Override
	public Double replace(Integer[] key, Double value)
	{
		if (key == null || value == null)
			return null;
		
		for (Integer keyInt : key)
			if (keyInt == null)
				return null;
		
		Arrays.sort(key);
		
		return super.replace(key, value);
	}
	
	@Override
	public boolean replace(Integer[] key, Double oldValue, Double newValue)
	{
		if (key == null || newValue == null)
			return false;
		
		for (Integer keyInt : key)
			if (keyInt == null)
				return false;
		
		Arrays.sort(key);
		
		return super.replace(key, oldValue, newValue);
	}
	
	@Override
	public void replaceAll(BiFunction<? super Integer[], ? super Double, ? extends Double> function)
	{
		super.replaceAll(function);
		super.forEach(removeNullValues);
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append('{');
		
		for (Integer[] myArr : this.keySet())
		{
			if (s.length() != 1)
				s.append("; ");
				
			StringBuffer ss = new StringBuffer();
			
			for (Integer myInt : myArr)
			{
				if (ss.length() != 0)
					ss.append(", ");
				
				ss.append(myInt);
			}
			
			s.append(ss);
			
			s.append(": ");
			s.append(String.format("%5.3f", this.get(myArr)));
		}
		
		s.append('}');
		
		return s.toString();
	}
	
	public static DiceRollVector diceRoll(int numDice, int sides)
	{
		DiceRollVector result = new DiceRollVector();
		
		ProbVector die = ProbVector.diceRoll(1, sides);
		
		for (int i = 0; i < numDice; i++)
			result = result.combine(die);
		
		return result;
	}
	
	public DiceRollVector combine(ProbVector pv)
	{
		DiceRollVector drvNew = new DiceRollVector();
		
		if (this.isEmpty())
		{
			for (Integer pvInt : pv.keySet())
			{
				Integer[] arrNew = {pvInt};
				drvNew.put(arrNew, pv.get(pvInt));
			}
			
			return drvNew;
		}
		
		for (Integer[] myArr : this.keySet())
		{
			for (Integer pvInt : pv.keySet())
			{
				Integer[] newArr = Arrays.copyOf(myArr, myArr.length + 1);
				newArr[myArr.length] = pvInt;
				
				Double newProb = drvNew.get(newArr);
				if (newProb == null)
					newProb = 0.0;
				
				newProb += this.get(myArr) * pv.get(pvInt);
				
				drvNew.put(newArr, newProb);
			}
		}
		
		return drvNew;
	}
	
	public ProbVector flatten(Function<Integer[], Integer> function)
	{
		ProbVector pvNew = new ProbVector();
		
		for (Integer[] myArr : this.keySet())
		{
			Integer newInt = function.apply(myArr);
			
			Double newProb = pvNew.get(newInt);
			if (newProb == null)
				newProb = 0.0;
			
			newProb += this.get(myArr);
			
			pvNew.put(newInt, newProb);
		}
		
		return pvNew;
	}
	
	public ProbVector flatten()
	{
		return flatten(sumFlatten);
	}
	
	private BiConsumer<Integer[], Double> putter = new BiConsumer<Integer[], Double>()
			{
				@Override
				public void accept(Integer[] key, Double value)
				{
					put(key, value);
				}
			};
			
	private BiConsumer<Integer[], Double> removeNullValues = new BiConsumer<Integer[], Double>()
			{
				@Override
				public void accept(Integer[] key, Double value)
				{
					if (value == null)
						remove(key);
				}
			};
			
	private static Function<Integer[], Integer> sumFlatten = new Function<Integer[], Integer>()
			{
				@Override
				public Integer apply(Integer[] key)
				{
					int total = 0;
					
					for (Integer myInt : key)
						total += myInt;
					
					return total;
				}
			};
}
