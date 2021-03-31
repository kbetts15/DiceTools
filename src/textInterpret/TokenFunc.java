package textInterpret;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public abstract class TokenFunc implements Function<List<Token>, Token>
{
	@Override
	public final Token apply(List<Token> li)
	{
		List<Object> objList = new LinkedList<Object>();
		for (Token t : li)
			objList.add(t.getVariable());
		
		Object variable = operate(objList);
		
		return new Token.VarToken(variable);
	}
	
	public abstract Object operate(List<? extends Object> li);
	public abstract String getName();
}