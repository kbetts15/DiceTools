import java.util.HashMap;
import java.util.function.Function;

public class Main
{

	public static void main(String[] args)
	{
//		HashMap<Integer, Double> hm = new HashMap<Integer, Double>();
		
//		ProbVector hm = new ProbVector();
//		
//		hm.put(1, 3.25);
//		System.out.println(hm.toString());
//		hm.put(2, 6.75);
//		System.out.println(hm.toString());
		
		testDRVput();
//		testPVrolls();
//		testDRVrolls();
//		testDRVflatten();
	}

	private static void testPVrolls()
	{
		System.out.println("##### Testing ProbVector rolls #####");
		
		for (int numDice = 0; numDice < 4; numDice++)
		{
			ProbVector pv = ProbVector.diceRoll(numDice, 6);
			System.out.printf("%dd6: %s\n", numDice, pv.toString());
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
		
		Function<Integer[], Integer> sumFlatten = new Function<Integer[], Integer>()
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
				
		ProbVector flattened = drv.flatten(sumFlatten);
		System.out.println(flattened.toString());
	}
	
	private static void testDRVput()
	{
		System.out.println("##### Testing DiceRollVector putting #####");
		
		ProbVector pv = new ProbVector();
		
		pv.put(1, new Double(0.5));
		pv.put(2, new Double(0.5));
		
		System.out.println(pv.toString());
		
		DiceRollVector drv = new DiceRollVector();
		
		Integer[] key1 = {1, 1};
		Integer[] key2 = {2, 1};
		
		drv.put(key1, new Double(0.5));
		drv.put(key2, new Double(0.5));
		
		System.out.println(drv.toString());
		
		System.out.println("drv values:");
		for (Double d : drv.values())
			System.out.printf("\t%.3f\n", d);
		
		Integer[] keyX = {1, 2};
		System.out.printf("get(keyX) = %.3f\n", drv.get(keyX));
	}
}
