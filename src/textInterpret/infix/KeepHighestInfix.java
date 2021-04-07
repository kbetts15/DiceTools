package textInterpret.infix;

import java.util.List;
import java.util.function.Function;

import diceTools.DiceNumber;
import diceTools.function.KeepN;

public class KeepHighestInfix extends ListModInfix
{

	@Override
	public Function<? super List<? extends DiceNumber>, List<DiceNumber>> listMod(int intMod)
	{
		return KeepN.keepHighestN(intMod);
	}

	@Override
	public String getName()
	{
		return "H";
	}

}
