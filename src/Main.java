import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Function;

import diceTools.DiceRollIterable;
import diceTools.DiceNumber;
import diceTools.DiceNumber.DiceInteger;
import diceTools.DicePoolMap;
import diceTools.ImmutableList;
import diceTools.ProbMap;
import diceTools.DiceRollMap;
import textInterpret.TextInterpret;
import textInterpret.Token;

public class Main
{

	public static void main(String[] args)
	{
		parseTest();
//		tokenizeTest();
//		groupTest();
//		iterSortTest();
//		testPutting();
//		testDRMrolls();
//		testDPMrolls();
//		testDPMflatten();
//		diceRollIterTest();
//		immListEqualsTest();
//		averageTest();
	}

	public static void testDRMrolls()
	{
		System.out.println("##### Testing DiceRollMap rolls #####");
		
		for (int numDice = 0; numDice < 4; numDice++)
		{
			System.out.printf("%dd6: ", numDice);
			DiceRollMap drm = DiceRollMap.diceRoll(numDice, 6);
			System.out.println(drm.toString());
		}
	}
	
	public static void testDPMrolls()
	{
		System.out.println("##### Testing DicePoolMap rolls #####");
		
		for (int numDice = 0; numDice < 4; numDice++)
		{
			System.out.printf("%dd2: ", numDice);
			DicePoolMap dpm = DicePoolMap.diceRoll(numDice, 2);
			System.out.println(dpm.toString());
		}
	}
	
	public static void testDPMflatten()
	{
		System.out.println("##### Testing DicePoolMap flattening #####");
		
		DicePoolMap dpm = DicePoolMap.diceRoll(3, 6);
		
		Function<List<? extends DiceNumber>, DiceNumber> sumFlatten = new Function<List<? extends DiceNumber>, DiceNumber>()
				{
					@Override
					public DiceNumber apply(List<? extends DiceNumber> key)
					{
						boolean doubleFound = false;
						int intTotal = 0;
						double doubleTotal = 0.0;
						
						for (DiceNumber myInt : key)
						{
							if (!doubleFound && !myInt.isInt())
							{
								doubleFound = true;
								doubleTotal = intTotal;
							}
							
							if (doubleFound)
								doubleTotal += myInt.doubleValue();
							else
								intTotal += myInt.intValue();
						}
						
						if (doubleFound)
							return new DiceNumber.DiceDouble(doubleTotal);
						else
							return new DiceNumber.DiceInteger(intTotal);
					}
				};
				
		DiceRollMap flattened = dpm.flatten(sumFlatten);
		System.out.println(flattened.toString());
	}
	
	public static void testPutting()
	{
		System.out.println("##### Testing DicePoolMap and DiceRollMap putting #####");
		
		DiceRollMap drm = new DiceRollMap();
		
		drm.put(new DiceNumber.DiceInteger(1), new Double(0.5));
		drm.put(new DiceNumber.DiceInteger(2), new Double(0.5));
		
		System.out.println(drm.toString());
		
		DicePoolMap dpm = new DicePoolMap();
		
		Integer[] key1arr = {1, 1};
		Integer[] key2arr = {2, 1};
		Integer[] key3arr = {1, 3};
		
		List<DiceInteger> key1 = new LinkedList<DiceInteger>();
		for (Integer val : key1arr)
			key1.add(new DiceNumber.DiceInteger(val));
		
		
		List<DiceInteger> key2 = new LinkedList<DiceInteger>();
		for (Integer val : key2arr)
			key2.add(new DiceNumber.DiceInteger(val));
		
		
		List<DiceInteger> key3 = new LinkedList<DiceInteger>();
		for (Integer val : key3arr)
			key3.add(new DiceNumber.DiceInteger(val));
		
		dpm.put(key1, new Double(0.2));
		dpm.put(key2, new Double(0.3));
		dpm.put(key3, new Double(0.5));
		
		System.out.println(dpm.toString());
		
		Integer[] keyXarr = {1, 3};
		List<Integer> keyX = new ArrayList<Integer>(Arrays.asList(keyXarr));
		
		System.out.printf("get(keyX) = %.3f\n", dpm.get(keyX));
		System.out.printf("get(keyXarr) = %.3f\n", dpm.get(keyXarr));
		
		System.out.printf("keyX: %s\n", keyX.toString());
		
		for (List<? extends DiceNumber> li : dpm.keySet())
		{
			System.out.printf("(keyX) %s %c= %s (li) | Hash codes %smatch\n",
					keyX.toString(),
					li.equals(keyX) ? '=' : '!',
					li.toString(),
					keyX.hashCode() == li.hashCode() ? "" : "do not ");
		}
	}
	
	public static void diceRollIterTest()
	{
		System.out.println("##### Testing DiceRollIterable #####");
		
		DiceRollIterable iter = new DiceRollIterable(3, 4);
		
		for (Entry<List<DiceNumber.DiceInteger>, Double> entry : iter)
			System.out.println(entry.getKey().toString());
	}
	
	public static void immListEqualsTest()
	{
		System.out.println("##### Testing ImmutableList .equals() #####");
		
		Integer[] arrA = {1, 2, 5};
		Integer[] arrB = {1, 2, 4};
		
		ImmutableList<Integer> immListA = new ImmutableList<Integer>(arrA);
		ImmutableList<Integer> immListB = new ImmutableList<Integer>(arrA);
		ImmutableList<Integer> immListC = new ImmutableList<Integer>(arrB);
		LinkedList<Integer> immListD = new LinkedList<Integer>(immListA);
		
		System.out.printf("immListA %c= immListB\n", immListA.equals(immListB) ? '=' : '!');
		System.out.printf("immListA %c= immListC\n", immListA.equals(immListC) ? '=' : '!');
		System.out.printf("immListB %c= immListC\n", immListB.equals(immListC) ? '=' : '!');
		System.out.printf("immListA %c= immListD\n", immListA.equals(immListD) ? '=' : '!');
	}
	
	public static void averageTest()
	{
		System.out.println("##### Testing averaging #####");
		
		DicePoolMap dpm = DicePoolMap.diceRoll(3, 6);
		System.out.printf("%15s: %s\n", "Dice rolled", dpm.toString());
		
		Function<List<? extends DiceNumber>, List<DiceNumber>> f = (li) -> {
			if (li == null)
				return null;
			
			List<DiceNumber> liNew = new LinkedList<DiceNumber>(li);
			
			if (li.isEmpty())
				return liNew;
			
			int minRoll = 7;
			int minPos = 0;
			
			for (int i = 0; i < li.size(); i++)
				if (liNew.get(i).intValue() < minRoll)
				{
					minRoll = i;
					minPos = i;
				}
			
			liNew.set(minPos, new DiceNumber.DiceInteger(6));
			
			return liNew;
		};
		
		DiceRollMap drm = dpm.flatten();
		System.out.printf("%15s: %s\n", "flattened", drm.toString());
		
		Map.Entry<DiceNumber, Double> entry;
		entry = ProbMap.getMode(drm);
		System.out.printf("\t%15s: %2d, %.3f\n", "mode", entry.getKey(), entry.getValue());
		entry = ProbMap.getMedian(drm);
		System.out.printf("\t%15s: %2d, %.3f\n", "median", entry.getKey(), entry.getValue());
		System.out.printf("\t%15s: %.3f\n", "mean", ProbMap.getMean(drm));
		
		dpm = (DicePoolMap) dpm.morph(f);
		System.out.printf("%15s: %s\n", "min->6", dpm.toString());
		
		drm = dpm.flatten();
		System.out.printf("%15s: %s\n", "flattened", drm.toString());
		
		entry = ProbMap.getMode(drm);
		System.out.printf("\t%15s: %2d, %.3f\n", "mode", entry.getKey(), entry.getValue());
		entry = ProbMap.getMedian(drm);
		System.out.printf("\t%15s: %2d, %.3f\n", "median", entry.getKey(), entry.getValue());
		System.out.printf("\t%15s: %.3f\n", "mean", ProbMap.getMean(drm));
	}
	
	public static void iterSortTest()
	{
		System.out.println("##### Testing DiceFileTools.iterableToSortedList #####");
		
		Integer[] intArr = {1, 3, 2, 7, 6, 5, 8, 4, 8, 2, 3};
		Iterable<Integer> intIter = new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator()
			{
				return Arrays.stream(intArr).iterator();
			}
		};
		
		System.out.printf("%20s: %s\n", "Initial array", Arrays.asList(intArr).toString());
		
		Comparator<Integer> intComp = Comparator.naturalOrder();
		List<Integer> intList = diceTools.DiceFileTools.iterableToSortedList(intIter, intComp);
		
		System.out.printf("%20s: %s\n", "Sorted list", intList.toString());
	}
	
	
	public static void parseTest()
	{
		System.out.println("##### Testing parsing #####");
		
		Scanner sc = new Scanner(System.in);
		
		final int maxEvalChars = 100;
		final String checkChars = String.format("%%.%ds", maxEvalChars + 1);
		final String longFormat = String.format("%%18s: %%.%ds ...\n", maxEvalChars);
		
		while (true)
		{
			System.out.print(">> ");
			String s = sc.nextLine();
			s = s.trim();
			
			if (s.length() == 0)
				continue;
			
			try
			{
			
				List<String> groupedList = TextInterpret.group(s);
				System.out.printf("%18s: %s\n", "Group characters", groupedList.toString());
				
				if (groupedList.size() > 0 && groupedList.get(0).equals("exit"))
					break;
				
				List<Token> tokenQueue = TextInterpret.tokenize(groupedList);
				System.out.printf("%18s: %s\n", "Make tokens", tokenQueue.toString());
				
				TextInterpret.validateTokenList(tokenQueue);
				
				Queue<Token> shuntedQueue = TextInterpret.shunt(tokenQueue);
				System.out.printf("%18s: %s\n", "Shunt to postfix", shuntedQueue.toString());
				
				Object result = TextInterpret.evaluate(shuntedQueue);
				
				if (result instanceof Map)
				{
					System.out.printf("%18s:\n", "Evaluate results");
					int numPrinted = 0;
					
					Map<?, ?> m = (Map<?, ?>) result;
					
					for (Object obj : m.entrySet())
					{
						Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
						
						if (numPrinted++ >= 100)
						{
							System.out.println("\t...\n");
							break;
						}
						
						System.out.printf("\t%s: %s\n", entry.getKey(), entry.getValue());
					}
				}
				else
				{
					String evalStr = String.format(checkChars, result);
					System.out.printf(evalStr.length() <= maxEvalChars ? "%18s: %s\n" : longFormat,
							"Evaluate results", TextInterpret.evaluate(shuntedQueue));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		sc.close();
	}
}
