import java.util.function.Function;

public class Main
{

	public static void main(String[] args)
	{
		testDRVflatten();
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
}
