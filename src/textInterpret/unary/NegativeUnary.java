package textInterpret.unary;

import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.DiceRollMap;

public class NegativeUnary extends ArgSortedUnary
{
	
	@Override
	public DicePoolMap operateCase(DicePoolMap operand)
	{
		UnaryOperator<List<? extends DiceNumber>> f = (x) -> {
			
			List<DiceNumber> liNew = new LinkedList<DiceNumber>();
			for (DiceNumber val : x)
				liNew.add(operateCase(val));
			return liNew;
		};
		
		return (DicePoolMap) operand.morph(f);
	}

	@Override
	public DiceRollMap operateCase(DiceRollMap operand)
	{
		UnaryOperator<DiceNumber> f = (x) -> {return operateCase(x);};
		return (DiceRollMap) operand.morph(f);
	}

	@Override
	public DiceNumber operateCase(DiceNumber operand)
	{
		if (operand.isInt())
			return new DiceNumber.DiceInteger(-operand.intValue());
		else
			return new DiceNumber.DiceDouble(-operand.doubleValue());
	}

	@Override
	public String getName()
	{
		return "-";
	}

}
