package textInterpret.infix;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.DiceRollMap;

public abstract class NumericInfix extends ArgSortedInfix
{
	private final BinaryOperator<DiceNumber> operator;
	
	public NumericInfix()
	{
		operator = new Infix();
	}
	
	@Override
	public abstract DiceNumber operateCase(DiceNumber a, DiceNumber b);

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
	public DiceRollMap operateCase(DicePoolMap dpm, DiceNumber n)
	{
		return operateCase(dpm.flatten(), n);
	}
	
	@Override
	public DiceRollMap operateCase(DiceNumber n, DicePoolMap dpm)
	{
		return operateCase(n, dpm.flatten());
	}
	
	@Override
	public DiceRollMap operateCase(DiceRollMap drm1, DiceRollMap drm2)
	{
		return (DiceRollMap) drm1.combine(operator, drm2);
	}
	
	@Override
	public DiceRollMap operateCase(DiceRollMap drm, DiceNumber n)
	{
		UnaryOperator<DiceNumber> unary = new UnaryOperator<DiceNumber>() {
			@Override
			public DiceNumber apply(DiceNumber arg)
			{
				return operator.apply(arg, n);
			}
		};
		
		return (DiceRollMap) drm.morph(unary);
	}
	
	@Override
	public DiceRollMap operateCase(DiceNumber n, DiceRollMap drm)
	{
		UnaryOperator<DiceNumber> unary = new UnaryOperator<DiceNumber>() {
			@Override
			public DiceNumber apply(DiceNumber arg)
			{
				return operator.apply(n, arg);
			}
		};
		
		return (DiceRollMap) drm.morph(unary);
	}
	
	private class Infix implements BinaryOperator<DiceNumber>
	{
		@Override
		public DiceNumber apply(DiceNumber a, DiceNumber b)
		{
			return operateCase(a, b);
		}
	}
}
