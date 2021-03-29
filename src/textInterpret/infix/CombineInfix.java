package textInterpret.infix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import diceTools.DiceRollVector;
import diceTools.ProbVector;
import textInterpret.TokenFuncInputTypeException;

public class CombineInfix extends ArgSortedInfix
{

	@Override
	public Object operateCase(DiceRollVector a, DiceRollVector b)
	{
		return a.combine(b);
	}

	@Override
	public Object operateCase(DiceRollVector a, ProbVector b)
	{
		return a.combine(b);
	}

	@Override
	public Object operateCase(DiceRollVector a, Integer b)
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
	public Object operateCase(ProbVector a, DiceRollVector b)
	{
		return operateCase(b, a);
	}

	@Override
	public Object operateCase(ProbVector a, ProbVector b)
	{
		DiceRollVector dicePool = new DiceRollVector(a);
		return dicePool.combine(b);
	}

	@Override
	public Object operateCase(ProbVector a, Integer b)
	{
		DiceRollVector dicePool = new DiceRollVector(a);
		return operateCase(dicePool, b);
	}

	@Override
	public Object operateCase(Integer a, DiceRollVector b)
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
