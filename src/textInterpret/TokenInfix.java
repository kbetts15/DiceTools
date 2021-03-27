package textInterpret;

import java.util.function.BinaryOperator;

public abstract class TokenInfix implements BinaryOperator<Token>
{
	@Override
	public final Token apply(Token a, Token b)
	{
		Object varA = a.getVariable();
		Object varB = b.getVariable();
		
		Object varNew = operate(varA, varB);
		
		Token newToken = new Token(TokenType.VAR, getName());
		newToken.setVariable(varNew);
		return newToken;
	}
	
	public abstract Object operate(Object objA, Object objB);
	public abstract String getName();
}