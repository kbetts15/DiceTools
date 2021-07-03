package diceTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides methods to save and load key-value pairs in files.
 * 
 * @author kieran
 */
public class DiceFileTools
{
	/**
	 * Save the contents of a {@link ProbMap} to a file
	 * 
	 * @param <T>			type of the keys accepted by the save <code>Function</code>
	 * @param pm			<code>ProbMap</code> containing the key-value pairs to be saved
	 * @param saveFunc		<code>Function</code> defining the rule by which keys are converted to <code>String</code>s
	 * @param f				<code>File</code> to which the data is saved
	 * @throws IOException	if an IO problem occurs while saving the data
	 */
	public static <T> void toFile(ProbMap<? extends T> pm, Function<T, String> saveFunc, File f) throws IOException
	{
		toFile(pm.entrySet(), saveFunc, null);
	}
	
	/**
	 * Save the contents of an {@link Iterable} of {@link java.util.Map.Entry Map.Entry}s
	 * to a file
	 * 
	 * @param <T>			Type of the keys of the {@code Map.Entry}s
	 * @param iter			{@code Iterable} for which each entry is to be saved
	 * @param saveFunc		{@code Function} defining the rule by which keys are converted to {@code String}s
	 * @param f				{@code File} to which the data is saved
	 * @throws IOException	if an IO problem occurs while saving the data
	 */
	public static <T> void toFile(Iterable<? extends Map.Entry<? extends T, Double>> iter,
			Function<T, String> saveFunc, File f) throws IOException
	{
		if (iter == null || saveFunc == null || f == null)
			throw new NullPointerException();
		
		FileWriter fw = new FileWriter(f);
		
		for (Map.Entry<? extends T, Double> entry : iter)
		{
			String keyStr = saveFunc.apply(entry.getKey());
			keyStr.replaceAll("[\n\r]", "");
			fw.write(keyStr);
			fw.write(':');
			fw.write(String.format("%d", entry.getValue()));
		}
		
		fw.close();
	}
	
	public static <T> void toFile(Iterable<? extends Map.Entry<? extends T, Double>> iter,
			Function<T, String> saveFunc, Comparator<? super T> comp, File f) throws IOException
	{
		if (iter == null || saveFunc == null || comp == null || f == null)
			throw new NullPointerException();
		
		Function<Map.Entry<? extends T, Double>, ? extends T> extractor = (e) -> {return e.getKey();};
		Comparator<Map.Entry<? extends T, Double>> entryComp =
				Comparator.comparing(extractor, comp);
		
		List<? extends Map.Entry<? extends T, Double>> entryList =
				iterableToSortedList(iter, entryComp);
		
		toFile(entryList, saveFunc, f);
	}
	
	public static <T> ProbMap<? super T> fromFile(Supplier<? extends ProbMap<? super T>> s,
			Function<String, T> loadFunc, File f) throws IOException
	{
		if (s == null || loadFunc == null || f == null)
			throw new NullPointerException();
		
		ProbMap<? super T> pm = s.get();
		pm.putAll(new EntryIterable<T>(f, loadFunc));
		
		return pm;
	}
	
	public static <T> List<T> iterableToSortedList(Iterable<T> iter, Comparator<? super T> comp)
	{
		if (iter == null || comp == null)
			throw new NullPointerException();
		
		LinkedList<T> sortedList = new LinkedList<T>();
		ListIterator<T> listIter = sortedList.listIterator();
		
		for (T t : iter)
		{
			if (t == null)
				continue;
			
			boolean goForwards = !listIter.hasNext();
			
			if (!listIter.hasNext())
				goForwards = false;
			else if (!listIter.hasPrevious())
				goForwards = true;
			else
			{
				T tCheck = listIter.next();
				int compResult = comp.compare(t, tCheck);
				if (compResult > 0)
					goForwards = true;
				else if (compResult < 0)
					goForwards = false;
				else //compResult == 0
				{
					listIter.add(t);
					continue;
				}
			}
			
			while (true)
			{
				T tCheck;
				
				if ((goForwards && !listIter.hasNext())
						|| (!goForwards && !listIter.hasPrevious()))
				{
					listIter.add(t);
					break;
				}
				
				if (goForwards)
					tCheck = listIter.next();
				else
					tCheck = listIter.previous();
				
				int compResult = comp.compare(t, tCheck);
				
				if (goForwards)
				{
					if (compResult > 0)
						continue;
					else if (compResult < 0)
						listIter.previous();
				}
				else
				{
					if (compResult < 0)
						continue;
					else if (compResult > 0)
						listIter.next();
				}
				
				listIter.add(t);
				break;
			}
		}
		
		return sortedList;
	}
	
	public static class EntryIterable<T> implements Iterable<Map.Entry<T, Double>>
	{
		private final File f;
		private final Function<String, T> loadFunc;
		
		public EntryIterable(File f, Function<String, T> loadFunc)
		{
			this.f = f;
			this.loadFunc = loadFunc;
		}

		@Override
		public Iterator<Entry<T, Double>> iterator()
		{
			try
			{
				return new EntryIterator<T>(f, loadFunc);
			}
			catch (IOException e)
			{
				return null;
			}
		}
	}
	
	public static class EntryIterator<T> implements Iterator<Map.Entry<T, Double>>, AutoCloseable
	{
		private final BufferedReader myReader;
		private final Function<String, T> loadFunc;
		
		FileEntry<T> nextEntry = null;
		
		public EntryIterator(File f, Function<String, T> loadFunc) throws IOException
		{
			myReader = new BufferedReader(new FileReader(f));
			this.loadFunc = loadFunc;
			
			nextEntry = getNextEntry();
		}

		@Override
		public boolean hasNext()
		{
			return nextEntry != null;
		}

		@Override
		public Map.Entry<T, Double> next()
		{
			if (nextEntry == null)
				throw new NoSuchElementException();
			
			FileEntry<T> giveEntry = nextEntry;
			nextEntry = getNextEntry();
			
			return giveEntry;
		}
		
		@Override
		public void close() throws IOException
		{
			myReader.close();
		}
		
		private FileEntry<T> getNextEntry()
		{
			while (true)
			{
				try
				{
					for (String line = myReader.readLine(); line != null; line = myReader.readLine())
					{
						FileEntry<T> e = stringToEntry(line);
						if (e != null)
							return e;
					}
				}
				catch (IOException e)
				{
					break;
				}
				catch (InvalidKeyStringException e)
				{
					//Do nothing
				}
			}
			
			try
			{
				this.close();
			}
			catch (IOException e) {}
			
			nextEntry = null;
			return null;
		}
		
		private FileEntry<T> stringToEntry(String s)
		{
			int separatePos = s.lastIndexOf(':');
			
			String keyStr = s.substring(0, separatePos);
			String valStr = s.substring(separatePos + 1);
			
			T key = loadFunc.apply(keyStr);
			Double val = Double.valueOf(valStr);
			
			return new FileEntry<T>(key, val);
		}
		
		private static class FileEntry<X> implements Map.Entry<X, Double>
		{
			private final X key;
			private final Double value;
			
			public FileEntry(X key, Double value)
			{
				this.key = key;
				this.value = value;
			}
			
			@Override
			public X getKey()
			{
				return key;
			}

			@Override
			public Double getValue()
			{
				return value;
			}

			@Override
			public Double setValue(Double value)
			{
				throw new UnsupportedOperationException();
			}
		}
		
		public static class InvalidKeyStringException extends RuntimeException
		{
			private static final long serialVersionUID = 1L;
			
		}
	}
}