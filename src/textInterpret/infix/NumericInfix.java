package textInterpret.infix;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import diceTools.DicePoolMap;
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
	public ProbVector operateCase(DicePoolMap dpm1, DicePoolMap dpm2)
	{
		return operateCase(dpm1.flatten(), dpm2.flatten());
	}
	
	@Override
	public ProbVector operateCase(DicePoolMap dpm, ProbVector pv)
	{
		return operateCase(dpm.flatten(), pv);
	}
	
	@Override
	public ProbVector operateCase(ProbVector pv, DicePoolMap dpm)
	{
		return operateCase(pv, dpm.flatten());
	}
	
	@Override
	public ProbVector operateCase(DicePoolMap dpm, Integer i)
	{
		return operateCase(dpm.flatten(), i);
	}
	
	@Override
	public ProbVector operateCase(Integer i, DicePoolMap dpm)
	{
		return operateCase(i, dpm.flatten());
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
