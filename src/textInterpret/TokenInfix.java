package textInterpret;

import java.util.function.BinaryOperator;

public abstract class TokenInfix implements BinaryOperator<Token>
{
	@Override
	public final Token apply(Token a, Token b)
	{
		if (a == null || b == null)
			throw new NullPointerException();
		
		Object varA = a.getVariable();
		Object varB = b.getVariable();
		
		Object varNew = operate(varA, varB);
		
		System.out.printf("\t\tvarNew: %s\n", varNew.toString());
		
		Token newToken = new Token(TokenType.VAR, varNew.toString());
		newToken.setVariable(varNew);
		return newToken;
	}
	
	public abstract Object operate(Object objA, Object objB);
	public abstract String getName();
}