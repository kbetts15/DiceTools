package textInterpret.infix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import diceTools.DicePoolMap;
import diceTools.ProbVector;
import textInterpret.TokenFuncInputTypeException;

public class CombineInfix extends ArgSortedInfix
{

	@Override
	public Object operateCase(DicePoolMap a, DicePoolMap b)
	{
		return a.combine(b);
	}

	@Override
	public Object operateCase(DicePoolMap a, ProbVector b)
	{
		return a.combine(b);
	}

	@Override
	public Object operateCase(DicePoolMap a, Integer b)
	{
		Function<? super List<Integer>, ? extends List<Integer>> f = (li) -> {
			
			List<Integer> liNew = new ArrayList<Integer>(li.size() + 1);
			liNew.addAll(li);
			liNew.add(b);
			return liNew;
			
		};

		return a.morph(f);
	}

	@Override
	public Object operateCase(ProbVector a, DicePoolMap b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(ProbVector a, ProbVector b)
	{
		DicePoolMap dicePool = new DicePoolMap(a);
		return dicePool.combine(b);
	}

	@Override
	public Object operateCase(ProbVector a, Integer b)
	{
		DicePoolMap dicePool = new DicePoolMap(a);
		return operateCase(dicePool, b);
	}

	@Override
	public Object operateCase(Integer a, DicePoolMap b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(Integer a, ProbVector b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(Integer a, Integer b)
	{
		throw new TokenFuncInputTypeException();
	}

	@Override
	public String getName()
	{
		return "~";
	}

}
