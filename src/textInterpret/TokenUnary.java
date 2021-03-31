package textInterpret;

import java.util.function.UnaryOperator;

public abstract class TokenUnary implements UnaryOperator<Token>
{
	@Override
	public final Token apply(Token t)
	{
		Object variable = t.getVariable();
		
		variable = operate(variable);
		
		return new Token.VarToken(variable);
	}
	
	public abstract Object operate(Object obj);
	public abstract String getName();
}