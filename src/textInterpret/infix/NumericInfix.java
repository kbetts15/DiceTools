package textInterpret.infix;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import diceTools.DicePoolMap;
import diceTools.DiceRollMap;

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
	public DiceRollMap operateCase(DicePoolMap dpm1, DicePoolMap dpm2)
	{
		return operateCase(dpm1.flatten(), dpm2.flatten());
	}
	
	@Override
	public DiceRollMap operateCase(DicePoolMap dpm, DiceRollMap drm)
	{
		return operateCase(dpm.flatten(), drm);
	}
	
	@Override
	public DiceRollMap operateCase(DiceRollMap drm, DicePoolMap dpm)
	{
		return operateCase(drm, dpm.flatten());
	}
	
	@Override
	public DiceRollMap operateCase(DicePoolMap dpm, Integer i)
	{
		return operateCase(dpm.flatten(), i);
	}
	
	@Override
	public DiceRollMap operateCase(Integer i, DicePoolMap dpm)
	{
		return operateCase(i, dpm.flatten());
	}
	
	@Override
	public DiceRollMap operateCase(DiceRollMap drm1, DiceRollMap drm2)
	{
		return (DiceRollMap) drm1.combine(operator, drm2);
	}
	
	@Override
	public DiceRollMap operateCase(DiceRollMap drm, Integer i)
	{
		UnaryOperator<Integer> unary = new UnaryOperator<Integer>() {
			@Override
			public Integer apply(Integer arg)
			{
				return operator.apply(arg, i);
			}
		};
		
		return (DiceRollMap) drm.morph(unary);
	}
	
	@Override
	public DiceRollMap operateCase(Integer i, DiceRollMap drm)
	{
		UnaryOperator<Integer> unary = new UnaryOperator<Integer>() {
			@Override
			public Integer apply(Integer arg)
			{
				return operator.apply(i, arg);
			}
		};
		
		return (DiceRollMap) drm.morph(unary);
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
