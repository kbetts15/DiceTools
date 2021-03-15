import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import diceTools.DiceRollIterable;
import diceTools.DiceRollVector;
import diceTools.ImmutableList;
import diceTools.ProbVector;

public class Main
{

	public static void main(String[] args)
	{
		testPutting();
		testPVrolls();
		testDRVrolls();
		testDRVflatten();
		diceRollIterTest();
		immListEqualsTest();
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
}
