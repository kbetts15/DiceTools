package textInterpret;

import java.util.function.UnaryOperator;

public abstract class TokenUnary implements UnaryOperator<Token>
{
	@Override
	public final Token apply(Token t)
	{
		Object variable = t.getVariable();
		
		variable = operate(variable);
		
		Token newToken = new Token(TokenType.VAR);
		newToken.setVariable(variable);
		return newToken;
	}
	
	public abstract Object operate(Object obj);
}