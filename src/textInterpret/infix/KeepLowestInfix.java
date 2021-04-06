package textInterpret.infix;

import java.util.List;
import java.util.function.Function;

import diceTools.function.KeepN;

public class KeepLowestInfix extends ListModInfix
{

	@Override
	public Function<? super List<Integer>, ? extends List<Integer>> listMod(Integer intMod)
	{
		return KeepN.keepLowestN(intMod);
	}

	@Override
	public String getName()
	{
		return "L";
	}

}
