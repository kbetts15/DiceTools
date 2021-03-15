package diceTools;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
public abstract class ProbMap<K> extends HashMap<K, Double> implements Supplier<ProbMap<K>>
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
		K keyK = makeKey(key);
		
		validateKey(keyK);
		
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.get(sanitizedKey);
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		K keyK = makeKey(key);
		
		validateKey(keyK);
		
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.containsKey(sanitizedKey);
	}
	
	@Override
	public Double remove(Object key)
	{
		K keyK = makeKey(key);
		
		validateKey(keyK);
		K sanitizedKey = sanitizeKey(keyK);
		
		return super.remove(sanitizedKey);
	}
	
	@Override
	public boolean remove(Object key, Object value)
	{
		K keyK = makeKey(key);
		
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
	 * Map each key in the <code>ProbMap</code> to a key in a new <code>ProbMap</code>,
	 * where the key type remains the same.
	 * @param f		rule for mapping keys
	 * @return		<code>ProbMap</code> containing the morphed data.
	 * 				The <code>ProbMap</code> returned is created using
	 * 				{@link java.util.function.Supplier#get Supplier.get}(),
	 * 				which is implemented by the subclass.
	 */
	public ProbMap<K> morph(Function<K, K> f)
	{
		return morph(f, this);
	}
	
	/**
	 * Map each key in the <code>ProbMap</code> to a key of a different type
	 * in a new <code>ProbMap</code>.
	 * @param f		rule for mapping keys
	 * @param s		{@link java.util.function.Supplier#Supplier Supplier}
	 * 				which can {@link java.util.function.Supplier#get get}()
	 * 				an empty <code>ProbMap</code> with keys of the new type.
	 * 				Note that all concrete <code>ProbMap&ltK&gt</code> subclasses
	 * 				implement <code>Supplier&ltProbMap&ltK&gt&gt</code>.
	 * @return		<code>ProbMap</code> containing the morphed data,
	 * 				created using <code>s.get()</code>
	 */
	public <T> ProbMap<T> morph(Function<K, T> f, Supplier<ProbMap<T>> s)
	{
		ProbMap<T> newMap = s.get();
		
		for (Entry<K, Double> entry : this.entrySet())
		{
			final K key = entry.getKey();
			final Double prob = entry.getValue();
			
			T newKey = f.apply(key);
			
			newMap.merge(newKey, prob, sumMerger);
		}
		
		return newMap;
	}

	/**
	 * Map each key-value pair in the <code>ProbMap</code>
	 * to one or several key-value pairs in a new <code>ProbMap</code>,
	 * where the key type remains the same.
	 * @param f		rule for mapping key-value pairs
	 * @return		<code>ProbMap</code> containing the forked data.
	 * 				The <code>ProbMap</code> returned is created using
	 * 				{@link java.util.function.Supplier#get Supplier.get}(),
	 * 				which is implemented by the subclass.
	 */
	public ProbMap<K> fork(Function<K, ProbMap<K>> f)
	{
		return fork(f, this);
	}
	
	/**
	 * Map each key-value pair in the <code>ProbMap</code> to a pair with a different
	 * key type in a new <code>ProbMap</code>.
	 * @param f		rule for mapping key-value pairs
	 * @param s		{@link java.util.function.Supplier#Supplier Supplier}
	 * 				which can {@link java.util.function.Supplier#get get}()
	 * 				an empty <code>ProbMap</code> with keys of the new type.
	 * 				Note that all concrete <code>ProbMap&ltK&gt</code> subclasses
	 * 				implement <code>Supplier&ltProbMap&ltK&gt&gt</code>.
	 * @return		<code>ProbMap</code> containing the forked data,
	 * 				created using <code>s.get()</code>
	 */
	public <T> ProbMap<T> fork(Function<K, ProbMap<T>> f, Supplier<ProbMap<T>> s)
	{
		ProbMap<T> newMap = s.get();
		
		for (Entry<K, Double> myEntry : this.entrySet())
		{
			final K myKey = myEntry.getKey();
			final Double myProb = myEntry.getValue();
			
			ProbMap<T> tempMap = f.apply(myKey);
			
			for (Entry<T, Double> tEntry : tempMap.entrySet())
			{
				final T tKey = tEntry.getKey();
				final Double tProb = tEntry.getValue();
				
				Double newProb = myProb * tProb;
				
				newMap.merge(tKey, newProb, sumMerger);
			}
		}
		
		return newMap;
	}
	
	/**
	 * Combine each key in the <code>ProbMap</code>
	 * with each key in a <code>ProbMap</code>.
	 * The probability of for each newly generated key is
	 * the product of the probabilities of the keys combined to create it.
	 * @param <T>	key type of the <code>ProbMap</code> argument
	 * 				and the resulting <code>ProbMap</code>
	 * @param f		rule for combining keys
	 * @param p		<code>ProbMap</code> to combine
	 * @return		<code>ProbMap</code> containing the combined data.
	 * 				The <code>ProbMap</code> returned is created using
	 * 				p.{@link java.util.function.Supplier#get get},
	 * 				which is implemented by the subclass.
	 */
	public <T> ProbMap<T> combine(BiFunction<K, T, T> f, ProbMap<T> p)
	{
		return combine(f, p, p);
	}
	
	/**
	 * Combine each key in the <code>ProbMap</code>
	 * with each key in a <code>ProbMap</code>.
	 * The probability of for each newly generated key is
	 * the product of the probabilities of the keys combined to create it.
	 * @param <X>	key type of the <code>ProbMap</code> argument
	 * @param <Y>	key type of the resulting <code>ProbMap</code>
	 * @param f		rule for combining keys
	 * @param p		<code>ProbMap</code> to combine
	 * @param s		{@link java.util.function.Supplier#Supplier Supplier}
	 * 				which can {@link java.util.function.Supplier#get get}()
	 * 				an empty <code>ProbMap</code> with keys of the new type.
	 * 				Note that all concrete <code>ProbMap&ltK&gt</code> subclasses
	 * 				implement <code>Supplier&ltProbMap&ltK&gt&gt</code>.
	 * @return		<code>ProbMap</code> containing the combined data.
	 * 				The <code>ProbMap</code> returned is created using
	 * 				s.{@link java.util.function.Supplier#get get},
	 * 				which is implemented by the subclass.
	 */
	public <X, Y> ProbMap<Y> combine(BiFunction<K, X, Y> f, ProbMap<X> p, Supplier<ProbMap<Y>> s)
	{
		ProbMap<Y> newMap = s.get();
		
		for (Entry<K, Double> myEntry : this.entrySet())
		{
			final K myKey = myEntry.getKey();
			final Double myProb = myEntry.getValue();
			
			for (Entry<X, Double> pEntry : p.entrySet())
			{
				final X pKey = pEntry.getKey();
				final Double pProb = pEntry.getValue();
				
				Y newKey = f.apply(myKey, pKey);
				Double newProb = myProb * pProb;
				
				newMap.merge(newKey, newProb, sumMerger);
			}
		}
		
		return newMap;
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
	 * Get a {@link java.util.function.Supplier#Supplier Supplier}
	 * which {@link java.util.function.Supplier#Supplier.get get}s
	 * this <code>ProbMap</code>.
	 * @return		<code>Supplier</code> which supplies this <code>ProbMap</code>
	 */
	public Supplier<ProbMap<K>> supplyMe()
	{
		return new Supplier<ProbMap<K>>() {

			@Override
			public ProbMap<K> get()
			{
				return ProbMap.this;
			}
			
		};
	}
	
	/**
	 * Make a key from an <code>Object</code>.
	 * If more complex behaviour than casting is required,
	 * subclasses should override this method.
	 * @param oKey	<code>Object
	 * @return
	 */
	public K makeKey(Object oKey)
	{
		@SuppressWarnings("unchecked")
		K key = (K) oKey;
		return key;
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
	 * Get a new, empty <code>ProbMap</code>.
	 * Concrete implementations should return instances of the implementing class.
	 */
	public abstract ProbMap<K> get();
	
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
