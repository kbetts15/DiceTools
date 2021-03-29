package textInterpret.infix;

import textInterpret.KeyValuePair;
import textInterpret.TokenInfix;

public class KeyValueInfix extends TokenInfix
{
	
	@Override
	public Object operate(Object objA, Object objB)
	{
		return new KeyValuePair(objA, objB);
	}

	@Override
	public String getName()
	{
		return ":";
	}

}
