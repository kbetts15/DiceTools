package textInterpret.unary;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.DiceRollMap;

public abstract class NumericUnary extends ArgSortedUnary
{

	@Override
	public DicePoolMap operateCase(DicePoolMap operand)
	{
		Function<List<? extends DiceNumber>, List<DiceNumber>> f =
				new Function<List<? extends DiceNumber>, List<DiceNumber>>() {

					@Override
					public List<DiceNumber> apply(List<? extends DiceNumber> li)
					{
						List<DiceNumber> liNew = new LinkedList<DiceNumber>();
						li.forEach((x) -> {liNew.add(operateCase(x));});
						return liNew;
					}
			
		};
		
		return (DicePoolMap) operand.morph(f);
	}

	@Override
	public DiceRollMap operateCase(DiceRollMap operand)
	{
		Function<DiceNumber, DiceNumber> f = new Function<DiceNumber, DiceNumber>() {

			@Override
			public DiceNumber apply(DiceNumber n)
			{
				return operateCase(n);
			}
			
		};
		
		return (DiceRollMap) operand.morph(f);
	}
	
	@Override
	public abstract DiceNumber operateCase(DiceNumber operand);

}
