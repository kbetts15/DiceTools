package textInterpret.infix;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import diceTools.DiceRollVector;
import diceTools.ProbVector;

public abstract class NumericInfix extends ArgSortedInfix
{
	private final BinaryOperator<Integer> operator;
	
	public NumericInfix()
	{
		operator = new Infix();
	}
	
	@Override
	public abstract Integer operateCase(Integer a, Integer b);

	@Override
	public ProbVector operateCase(DiceRollVector drv1, DiceRollVector drv2)
	{
		return operateCase(drv1.flatten(), drv2.flatten());
	}
	
	@Override
	public ProbVector operateCase(DiceRollVector drv, ProbVector pv)
	{
		return operateCase(drv.flatten(), pv);
	}
	
	@Override
	public ProbVector operateCase(ProbVector pv, DiceRollVector drv)
	{
		return operateCase(pv, drv.flatten());
	}
	
	@Override
	public ProbVector operateCase(DiceRollVector drv, Integer i)
	{
		return operateCase(drv.flatten(), i);
	}
	
	@Override
	public ProbVector operateCase(Integer i, DiceRollVector drv)
	{
		return operateCase(i, drv.flatten());
	}
	
	@Override
	public ProbVector operateCase(ProbVector pv1, ProbVector pv2)
	{
		return (ProbVector) pv1.combine(operator, pv2);
	}
	
	@Override
	public ProbVector operateCase(ProbVector pv, Integer i)
	{
		UnaryOperator<Integer> unary = new UnaryOperator<Integer>() {
			@Override
			public Integer apply(Integer arg)
			{
				return operator.apply(arg, i);
			}
		};
		
		return (ProbVector) pv.morph(unary);
	}
	
	@Override
	public ProbVector operateCase(Integer i, ProbVector pv)
	{
		UnaryOperator<Integer> unary = new UnaryOperator<Integer>() {
			@Override
			public Integer apply(Integer arg)
			{
				return operator.apply(i, arg);
			}
		};
		
		return (ProbVector) pv.morph(unary);
	}
	
	private class Infix implements BinaryOperator<Integer>
	{
		@Override
		public Integer apply(Integer a, Integer b)
		{
			return operateCase(a, b);
		}
	}
}
