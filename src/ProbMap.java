import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Stores the probabilities of observing discrete events (such as dice rolls or darts scores).
 * <p>Events and their probabilities are stored as key-value pairs,
 * with {@link java.util.HashMap#HashMap} as the underlying implementation.
 * Event probabilities are stored as <code>Double</code>s,
 * which are permitted to take values greater than 1 or less than 0, but never <code>null</code>.
 * 
 * @author kieran
 *
 * @param <K>	Type of the events
 */
public abstract class ProbMap<K> extends HashMap<K, Double>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums together <code>Double</code> pairs
	 */
	protected static final BiFunction<Double, Double, Double> sumMerger;
	
	/**
	 * <code>put</code>s key-value pairs into the ProbMap
	 */
	protected final BiConsumer<K, Double> putter;
	
	/**
	 * <code>remove</code>s key-value pairs from the ProbMap
	 */
	protected final BiConsumer<K, Double> invalidRemover;
	
	static
	{
		sumMerger = new BiFunction<Double, Double, Double>()
		{
			@Override
			public Double apply(Double a, Double b)
			{
				if (a == null || b == null)
					throw new NullPointerException(String.format("%s%s",
							a == null ? "a == null" : "",
							b == null ? "b == null" : ""));
				
				return a + b;
			}
		};
	}
	
	public ProbMap()
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
	
	/**
	 * Throw an exception if the provided key is invalid
	 * @param key	the key to validate
	 */
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
		//TODO: This doesn't really work due to binary fractions - replace it with something to sum the probability
		
		double total = 0;
		for (Double prob : this.values())
			total += prob;
		System.out.printf("{prob = %f}", total);
		return total == 1.0;
	}
	
	/**
	 * Check whether a key is valid for use in the ProbMap.
	 * Note that all keys are sanitized with {@link sanitizeKey} after validation,
	 * before they are presented to the underlying HashMap implementation.
	 * @param key	key to check validity
	 * @return		true is the key is valid, false otherwise
	 */
	public abstract boolean keyIsValid(K key);
	
	/**
	 * Sanitize a key.
	 * Called on all keys after they are verified with {@link validateKey},
	 * before they are presented to the underlying HashMap implementation.
	 * @param key	key to sanitize
	 * @return		the sanitized key
	 */
	public abstract K sanitizeKey(K key);
	
	/**
	 * Exception to be thrown upon receiving an invalid key
	 * 
	 * @author kieran
	 */
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
