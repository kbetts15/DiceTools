package textInterpret.infix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
import textInterpret.TokenFuncInputTypeException;

public class CombineInfix extends ArgSortedInfix
{

	@Override
	public Object operateCase(DicePoolMap a, DicePoolMap b)
	{
		return a.combine(b);
	}

	@Override
	public Object operateCase(DicePoolMap a, DiceRollMap b)
	{
		return a.combine(b);
	}

	@Override
	public Object operateCase(DicePoolMap a, DiceNumber b)
	{
		Function<? super List<? extends DiceNumber>, List<DiceNumber>> f = (li) -> {
			
			List<DiceNumber> liNew = new ArrayList<DiceNumber>(li.size() + 1);
			liNew.addAll(li);
			liNew.add(b);
			return liNew;
			
		};

		return a.morph(f);
	}

	@Override
	public Object operateCase(DiceRollMap a, DicePoolMap b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(DiceRollMap a, DiceRollMap b)
	{
		DicePoolMap dicePool = new DicePoolMap(a);
		return dicePool.combine(b);
	}

	@Override
	public Object operateCase(DiceRollMap a, DiceNumber b)
	{
		DicePoolMap dicePool = new DicePoolMap(a);
		return operateCase(dicePool, b);
	}

	@Override
	public Object operateCase(DiceNumber a, DicePoolMap b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(DiceNumber a, DiceRollMap b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(DiceNumber a, DiceNumber b)
	{
		throw new TokenFuncInputTypeException();
	}

	@Override
	public String getName()
	{
		return "~";
	}

}
