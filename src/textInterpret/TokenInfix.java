package textInterpret;

import java.util.function.BinaryOperator;

public abstract class TokenInfix implements BinaryOperator<Token>
{
	@Override
	public final Token apply(Token a, Token b)
	{
		if (a == null || b == null)
			throw new NullPointerException(String.format("Argument %s of %s.apply cannot be null",
					getClass().getName(),
					a == null ? (b == null ? "a, b" : "a") : ("b")));
		
		Object varA = a.getVariable();
		Object varB = b.getVariable();
		
		Object varNew = operate(varA, varB);
		
		return new Token.VarToken(varNew);
	}
	
	public abstract Object operate(Object objA, Object objB);
	public abstract String getName();
}