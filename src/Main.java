import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.function.Function;

import diceTools.DiceRollIterable;
import diceTools.DiceRollVector;
import diceTools.ImmutableList;
import diceTools.ProbMap;
import diceTools.ProbVector;
import textInterpret.TextInterpret;

public class Main
{

	public static void main(String[] args)
	{
//		lexTest();
//		iterSortTest();
//		testPutting();
//		testPVrolls();
//		testDRVrolls();
//		testDRVflatten();
//		diceRollIterTest();
//		immListEqualsTest();
//		averageTest();
	}

	private static void testPVrolls()
	{
		System.out.println("##### Testing ProbVector rolls #####");
		
		for (int numDice = 0; numDice < 4; numDice++)
		{
			System.out.printf("%dd6: ", numDice);
			ProbVector pv = ProbVector.diceRoll(numDice, 6);
			System.out.println(pv.toString());
		}
	}
	
	private static void testDRVrolls()
	{
		System.out.println("##### Testing DiceRollVector rolls #####");
		
		for (int numDice = 0; numDice < 4; numDice++)
		{
			System.out.printf("%dd2: ", numDice);
			DiceRollVector drv = DiceRollVector.diceRoll(numDice, 2);
			System.out.println(drv.toString());
		}
	}
	
	private static void testDRVflatten()
	{
		System.out.println("##### Testing DiceRollVector flattening #####");
		
		DiceRollVector drv = DiceRollVector.diceRoll(3, 6);
		
		Function<List<Integer>, Integer> sumFlatten = new Function<List<Integer>, Integer>()
				{
					@Override
					public Integer apply(List<Integer> key)
					{
						int total = 0;
						
						for (Integer myInt : key)
							total += myInt;
						
						return total;
					}
				};
				
		ProbVector flattened = drv.flatten(sumFlatten);
		System.out.println(flattened.toString());
	}
	
	private static void testPutting()
	{
		System.out.println("##### Testing DiceRollVector and ProbVector putting #####");
		
		ProbVector pv = new ProbVector();
		
		pv.put(1, new Double(0.5));
		pv.put(2, new Double(0.5));
		
		System.out.println(pv.toString());
		
		DiceRollVector drv = new DiceRollVector();
		
		Integer[] key1arr = {1, 1};
		Integer[] key2arr = {2, 1};
		Integer[] key3arr = {1, 3};
		
		List<Integer> key1 = new LinkedList<Integer>(Arrays.asList(key1arr));
		List<Integer> key2 = new LinkedList<Integer>(Arrays.asList(key2arr));
		List<Integer> key3 = new ArrayList<Integer>(Arrays.asList(key3arr));
		
		drv.put(key1, new Double(0.2));
		drv.put(key2, new Double(0.3));
		drv.put(key3, new Double(0.5));
		
		System.out.println(drv.toString());
		
		Integer[] keyXarr = {1, 3};
		List<Integer> keyX = new ArrayList<Integer>(Arrays.asList(keyXarr));
		
		System.out.printf("get(keyX) = %.3f\n", drv.get(keyX));
		System.out.printf("get(keyXarr) = %.3f\n", drv.get(keyXarr));
		
		System.out.printf("keyX: %s\n", keyX.toString());
		
		for (List<Integer> li : drv.keySet())
		{
			System.out.printf("(keyX) %s %c= %s (li) | Hash codes %smatch\n",
					keyX.toString(),
					li.equals(keyX) ? '=' : '!',
					li.toString(),
					keyX.hashCode() == li.hashCode() ? "" : "do not ");
		}
	}
	
	private static void diceRollIterTest()
	{
		System.out.println("##### Testing DiceRollIterable #####");
		
		DiceRollIterable iter = new DiceRollIterable(3, 4);
		
		for (Entry<List<Integer>, Double> entry : iter)
			System.out.println(entry.getKey().toString());
	}
	
	private static void immListEqualsTest()
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
	
	private static void averageTest()
	{
		System.out.println("##### Testing averaging #####");
		
		DiceRollVector drv = DiceRollVector.diceRoll(3, 6);
		System.out.printf("%15s: %s\n", "Dice rolled", drv.toString());
		
		Function<List<Integer>, List<Integer>> f = (li) -> {
			if (li == null)
				return null;
			
			List<Integer> liNew = new LinkedList<Integer>(li);
			
			if (li.isEmpty())
				return liNew;
			
			int minRoll = 7;
			int minPos = 0;
			
			for (int i = 0; i < li.size(); i++)
				if (liNew.get(i) < minRoll)
				{
					minRoll = i;
					minPos = i;
				}
			
			liNew.set(minPos, 6);
			
			return liNew;
		};
		
		ProbVector pv = drv.flatten();
		System.out.printf("%15s: %s\n", "flattened", pv.toString());
		
		Map.Entry<Integer, Double> entry;
		entry = ProbMap.getMode(pv);
		System.out.printf("\t%15s: %2d, %.3f\n", "mode", entry.getKey(), entry.getValue());
		entry = ProbMap.getMedian(pv);
		System.out.printf("\t%15s: %2d, %.3f\n", "median", entry.getKey(), entry.getValue());
		System.out.printf("\t%15s: %.3f\n", "mean", ProbMap.getMean(pv));
		
		drv = (DiceRollVector) drv.morph(f);
		System.out.printf("%15s: %s\n", "min->6", drv.toString());
		
		pv = drv.flatten();
		System.out.printf("%15s: %s\n", "flattened", pv.toString());
		
		entry = ProbMap.getMode(pv);
		System.out.printf("\t%15s: %2d, %.3f\n", "mode", entry.getKey(), entry.getValue());
		entry = ProbMap.getMedian(pv);
		System.out.printf("\t%15s: %2d, %.3f\n", "median", entry.getKey(), entry.getValue());
		System.out.printf("\t%15s: %.3f\n", "mean", ProbMap.getMean(pv));
	}
	
	private static void iterSortTest()
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
	
	private static void lexTest()
	{
		Scanner sc = new Scanner(System.in);
		
		while (true)
		{
			System.out.print(">> ");
			String s = sc.nextLine();
			List<String> parseList = TextInterpret.group(s);
			System.out.println(parseList.toString());
		}
	}
}
