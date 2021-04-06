package textInterpret.infix;

import java.util.List;
import java.util.function.Function;

import diceTools.function.KeepN;

public class KeepHighestInfix extends ListModInfix
{

	@Override
	public Function<? super List<Integer>, ? extends List<Integer>> listMod(Integer intMod)
	{
		return KeepN.keepHighestN(intMod);
	}

	@Override
	public String getName()
	{
		return "H";
	}

}
