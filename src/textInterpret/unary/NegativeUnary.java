package textInterpret.unary;

import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

import diceTools.DicePoolMap;
import diceTools.ProbVector;

public class NegativeUnary extends ArgSortedUnary
{
	
	@Override
	public Object operateCase(DicePoolMap operand)
	{
		UnaryOperator<List<Integer>> f = (x) -> {
			List<Integer> liNew = new LinkedList<Integer>();
			for (Integer i : x)
				liNew.add(-i);
			return liNew;
		};
		return operand.morph(f);
	}

	@Override
	public Object operateCase(ProbVector operand)
	{
		UnaryOperator<Integer> f = (x) -> {return -x;};
		return operand.morph(f);
	}

	@Override
	public Object operateCase(Integer operand)
	{
		return -operand;
	}

	@Override
	public String getName()
	{
		return "-";
	}

}
