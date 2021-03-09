import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class ProbMap<K> extends HashMap<K, Double>
{
	private static final long serialVersionUID = 1L;
	
	protected static final BiFunction<Double, Double, Double> sumMerger;
	
	protected final BiConsumer<K, Double> putter;
	protected final BiConsumer<K, Double> invalidRemover;
	
	static
	{
		sumMerger = new BiFunction<Double, Double, Double>()
		{
			@Override
			public Double apply(Double a, Double b)
			{
				return a + b;
			}
		};
	}
	
	public ProbMap(K keyInstance)
	{
		putter = new BiConsumer<K, Double>()
				{
					@Override
					public void accept(K key, Double value)
					{
						ProbMap.this.put(key, value);
					}
				};
				
		invalidRemover = new BiConsumer<K, Double>()
				{
					@Override
					public void accept(K key, Double value)
					{
						if (!keyIsValid(key) || value == null)
							ProbMap.this.remove(key);
					}
				};
	}
	
	@Override
	public Double put(K key, Double value)
	{
		validateKey(key);
		
		if (value == null)
			throw new NullPointerException();
		
		K sanitizedKey = sanitizeKey(key);
		
		System.out.printf("hashcode: %d->%d\n", key.hashCode(), sanitizedKey.hashCode());
		
		return super.put(sanitizedKey, value);
	}
	
	@Override
	public Double get(Object key)
	{
		@SuppressWarnings("unchecked")
		K keyK = (K) key;
		
		validateKey(keyK);
		
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.get(sanitizedKey);
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		@SuppressWarnings("unchecked")
		K keyK = (K) key;
		
		validateKey(keyK);
		
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.containsKey(sanitizedKey);
	}
	
	@Override
	public Double remove(Object key)
	{
		@SuppressWarnings("unchecked")
		K keyK = (K) key;
		
		validateKey(keyK);
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.remove(sanitizedKey);
	}
	
	@Override
	public boolean remove(Object key, Object value)
	{
		@SuppressWarnings("unchecked")
		K keyK = (K) key;
		
		validateKey(keyK);
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.remove(sanitizedKey, value);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends Double> map)
	{
		map.forEach(putter);
	}
	
	@Override
	public Double putIfAbsent(K key, Double value)
	{
		validateKey(key);
		
		if (value == null)
			throw new NullPointerException();
		
		K sanitizedKey = sanitizeKey(key);
		
		return super.putIfAbsent(sanitizedKey, value);
	}
	
	@Override
	public Double replace(K key, Double value)
	{
		validateKey(key);
		
		if (value == null)
			throw new NullPointerException();
		
		K sanitizedKey = sanitizeKey(key);
		
		return super.replace(sanitizedKey, value);
	}
	
	@Override
	public boolean replace(K key, Double oldValue, Double newValue)
	{
		validateKey(key);
		
		if (newValue == null)
			throw new NullPointerException();
		
		K sanitizedKey = sanitizeKey(key);
		
		return super.replace(sanitizedKey, oldValue, newValue);
	}
	
	@Override
	public void replaceAll(BiFunction<? super K, ? super Double, ? extends Double> function)
	{
		super.replaceAll(function);
		super.forEach(invalidRemover);
	}
	
	@Override
	public Double merge(K key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction)
	{
		validateKey(key);
		K sanitizedKey = sanitizeKey(key);
		
		return super.merge(sanitizedKey, value, remappingFunction);
	}
	
	@Override
	public Double compute(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction)
	{
		validateKey(key);
		K sanitizedKey = sanitizeKey(key);
		
		return super.compute(sanitizedKey, remappingFunction);
	}
	
	@Override
	public Double computeIfAbsent(K key, Function<? super K, ? extends Double> mappingFunction)
	{
		validateKey(key);
		K sanitizedKey = sanitizeKey(key);
		
		return super.computeIfAbsent(sanitizedKey, mappingFunction);
	}
	
	@Override
	public Double computeIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction)
	{
		validateKey(key);
		K sanitizedKey = sanitizeKey(key);

		return super.computeIfPresent(sanitizedKey, remappingFunction);
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append("{");
		
		for (Entry<K, Double> entry : this.entrySet())
		{
			if (s.length() != 1)
				s.append(", ");
			
			s.append(entry.getKey().toString());
			s.append(": ");
			s.append(String.format("%5.3f", entry.getValue()));
		}
		
		s.append("}");
		return s.toString();
	}
	
	private void validateKey(K key)
	{
		if (!keyIsValid(key))
			throw new InvalidKeyException();
	}
	
	/**
	 * Get whether the stored probabilities sum to 1.0
	 * @return true if the stored probabilities sum to 1.0, false otherwise
	 */
	public boolean hasValidProb()
	{
		double total = 0;
		for (Double prob : this.values())
			total += prob;
		return total == 1.0;
	}
	
	public abstract boolean keyIsValid(K key);
	public abstract K sanitizeKey(K key);
	
	public static class InvalidKeyException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;

		public InvalidKeyException()
		{
			super();
		}
		
		public InvalidKeyException(String message)
		{
			super(message);
		}
	}
}
