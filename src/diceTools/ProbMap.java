package diceTools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Stores the probabilities of observing discrete events (such as dice rolls or darts scores).
 * <p>Events and their probabilities are stored as key-value pairs,
 * with {@link java.util.HashMap#HashMap HashMap} as the underlying implementation.
 * Event probabilities are stored as <code>Double</code>s,
 * which are permitted to take values greater than 1 or less than 0, but never <code>null</code>.
 * 
 * @author kieran
 *
 * @param <K>	Type of the keys
 */
public abstract class ProbMap<K> extends HashMap<K, Double> implements Supplier<ProbMap<K>>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sums together <code>Double</code> pairs
	 */
	private static final BiFunction<Double, Double, Double> sumMerger;
	
	/**
	 * Gets the <code>Double</code> value of a <code>Number</code>
	 */
	private static final Function<Number, Double> toDouble;
	
	/**
	 * <code>put</code>s key-value pairs into the ProbMap
	 */
	private final BiConsumer<K, Double> putter;
	
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
		
		toDouble = new Function<Number, Double>()
		{
			@Override
			public Double apply(Number num)
			{
				return num.doubleValue();
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
	
	public <T extends K> void putAll(Iterable<Entry<T, Double>> it)
	{
		for (Entry<T, Double> entry : it)
			put(entry.getKey(), entry.getValue());
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
		for (Entry<K, Double> entry : this.entrySet())
		{
			final K key = entry.getKey();
			final Double prob = entry.getValue();
			
			Double newProb = function.apply(key, prob);
			
			this.put(key, newProb);
		}
	}
	
	@Override
	public Double merge(K key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction)
	{
		validateKey(key);
		K sanitizedKey = sanitizeKey(key);
		
		return super.merge(sanitizedKey, value, remappingFunction);
	}
	
	/**
	 * Perform {@link java.util.HashMap#merge HashMap.merge} using
	 * {@link diceTools.ProbMap#sumMerger sumMerger} for the <code>BiFunction</code>
	 * @param key		key with which the resulting value is to be associated
	 * @param value		the non-null value to be merged with the existing value
	 * 					associated with the key or, if no existing value or a
	 * 					null value is associated with the key, to be associated
	 * 					with the key
	 * @return			the new value associated with the specified key, or null
	 * 					if no value is associated with the key
	 */
	public Double merge(K key, Double value)
	{
		return merge(key, value, sumMerger);
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
	 * Find the mode average of a <code>ProbMap</code>,
	 * and return it as a {@link java.util.HashMap#Entry HashMap.Entry}.
	 * @param <T>	type of the <code>ProbMap</code> keys
	 * @param pm	<code>ProbMap</code> to find the mode average of
	 * @return		<code>Entry</code> with the highest probability in the <code>ProbMap</code>
	 */
	public static <T> Entry<T, Double> getMode(ProbMap<T> pm)
	{
		Entry<T, Double> mode = null;
		
		for (Entry<T, Double> entry : pm.entrySet())
			if (mode == null || entry.getValue() > mode.getValue())
				mode = entry;
		
		return mode;
	}
	
	/**
	 * Find the mean average of a <code>ProbMap</code>
	 * by converting keys into <code>Double</code>s.
	 * @param <X>		type of the <code>ProbMap</code> keys
	 * @param <Y>		type which the <code>Function</code> accepts,
	 * 					which includes <code>X</code>
	 * @param pm		<code>ProbMap</code> for which the mean average is to be found
	 * @param function	rule for converting keys into <code>Double</code>s
	 * @return			the sum of key <code>Double</code>s multiplied by their probabilities
	 **/
	public static <X extends Y, Y> Double getMean(ProbMap<X> pm, Function<Y, Double> function)
	{
		if (pm.isEmpty())
			return null;
		
		Double mean = 0.0;
		
		for (Entry<X, Double> entry : pm.entrySet())
			mean += function.apply(entry.getKey()) * entry.getValue();
		
		return mean;
	}
	
	/**
	 * Find the mean average of a <code>ProbMap</code> of <code>Number</code>s
	 * @param pm	<code>ProbMap</code> for which the mean average is to be found
	 * @return		the sum of key <code>Double</code>s multiplied by their probabilities
	 */
	public static <T extends Number> Double getMean(ProbMap<T> pm)
	{
		return ProbMap.getMean(pm, toDouble);
	}
	
	/**
	 * Find the median average of a <code>ProbMap</code> when keys are ordered.
	 * @param comp	rule for ordering keys
	 * @return		the first key which, when its probability is summed with the
	 * 				probabilities of all preceding keys, results in a probability
	 * 				greater than or equal to the sum of all the probabilities
	 */
	public static <T> Entry<T, Double> getMedian(ProbMap<T> pm, Comparator<T> comp)
	{
		if (pm.isEmpty())
			return null;
		
		List<Entry<T, Double>> entryList = new ArrayList<Entry<T, Double>>(pm.entrySet());
		
		Comparator<Entry<T, Double>> entryComp = Comparator.comparing((e)->{return e.getKey();}, comp);
		
		entryList.sort(entryComp);
		Double probTarget = 0.0;
		
		for (Double prob : pm.values())
			probTarget += prob;
		
		probTarget /= 2;
		
		Double probSoFar = 0.0;
		
		for (Entry<T, Double> entry : entryList)
		{
			probSoFar += entry.getValue();
			if (probSoFar >= probTarget)
				return entry;
		}
		
		//This should never be reached
		return null;
	}
		
	/**
	 * Find the median average of a <code>ProbMap</code> when keys are ordered.
	 * @param pm	<code>ProbMap</code> for which the median is to be found
	 * @return		the first key which, when its probability is summed with the
	 * 				probabilities of all preceding keys, results in a probability
	 * 				greater than or equal to the sum of all the probabilities
	 */
	public static <T extends Comparable<T>> Entry<T, Double> getMedian(ProbMap<T> pm)
	{
		Comparator<T> comp = Comparator.naturalOrder();
		
		return getMedian(pm, comp);
	}
	
	/**
	 * Make a key from an <code>Object</code>.
	 * If more complex behaviour than casting is required,
	 * subclasses should override this method.
	 * @param oKey	<code>Object</code> to be made into a key
	 * @return		key with correct type
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
