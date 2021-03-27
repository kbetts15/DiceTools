package textInterpret;

public final class Token
{
	public final TokenType type;
	private final String name;
	
	//VAR
	private Object var = null;
	
	//FUNC_UNARY
	private TokenUnary funcUnary = null;
	
	//FUNC_INFIX
	private TokenInfix funcInfix = null;
	
	//FUNC_ARGS
	private TokenFunc funcArgs = null;
	private int numArgs = 0;
	
	//BRACKET_OPEN
	private Token funcOwner = null;
	
	//BRACKET_CLOSE
	private char openSymbol = '\0';
	
	public Token(TokenType type, String name)
	{
		this.type = type;
		this.name = name;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder(name);
		sb.append('<');
		
		switch (type)
		{
			case VAR:
				sb.append('V');
				break;
			case BRACKET_OPEN:
				sb.append('O');
				break;
			case BRACKET_CLOSE:
				sb.append('C');
				break;
			case FUNC_UNARY:
				sb.append('U');
				break;
			case FUNC_INFIX:
				sb.append('I');
				break;
			case FUNC_ARGS:
				sb.append('A');
				sb.append(numArgs);
				break;
		}
		
		sb.append('>');
		return sb.toString();
	}
	
	public Object getVariable()
	{
		if (type != TokenType.VAR)
			throw new TokenTypeMismatchException();
		
		return var;
	}

	public void setVariable(Object var)
	{
		if (type != TokenType.VAR)
			throw new TokenTypeMismatchException();
		
		this.var = var;
	}

	public TokenUnary getFuncUnary()
	{
		if (type != TokenType.FUNC_UNARY)
			throw new TokenTypeMismatchException();
		
		return funcUnary;
	}

	public void setFuncUnary(TokenUnary funcUnary)
	{
		if (type != TokenType.FUNC_UNARY)
			throw new TokenTypeMismatchException();
		
		this.funcUnary = funcUnary;
	}

	public TokenInfix getFuncInfix()
	{
		if (type != TokenType.FUNC_INFIX)
			throw new TokenTypeMismatchException();
		
		return funcInfix;
	}

	public void setFuncInfix(TokenInfix funcInfix)
	{
		if (type != TokenType.FUNC_INFIX)
			throw new TokenTypeMismatchException();
		
		this.funcInfix = funcInfix;
	}

	public TokenFunc getFuncArgs()
	{
		if (type != TokenType.FUNC_ARGS)
			throw new TokenTypeMismatchException();
		
		return funcArgs;
	}

	public void setFuncArgs(TokenFunc funcArgs)
	{
		if (type != TokenType.FUNC_ARGS)
			throw new TokenTypeMismatchException();
		
		this.funcArgs = funcArgs;
	}
	
	public char getOpenSymbol()
	{
		if (type != TokenType.BRACKET_CLOSE)
			throw new TokenTypeMismatchException();
		
		return openSymbol;
	}

	public void setOpenSymbol(char openSymbol)
	{
		if (type != TokenType.BRACKET_CLOSE)
			throw new TokenTypeMismatchException();
		
		this.openSymbol = openSymbol;
	}

	public int getNumArgs()
	{
		if (type != TokenType.FUNC_ARGS)
			throw new TokenTypeMismatchException();
		
		return numArgs;
	}

	public void setNumArgs(int numArgs)
	{
		if (type != TokenType.FUNC_ARGS)
			throw new TokenTypeMismatchException();
		
		this.numArgs = numArgs;
	}
	
	public void incNumArgs()
	{
		if (type != TokenType.FUNC_ARGS)
			throw new TokenTypeMismatchException();
		
		this.numArgs++;
	}

	public Token getFuncOwner()
	{
		if (type != TokenType.BRACKET_OPEN)
			throw new TokenTypeMismatchException();
		
		return funcOwner;
	}

	public void setFuncOwner(Token funcOwner)
	{
		if (type != TokenType.BRACKET_OPEN)
			throw new TokenTypeMismatchException();
		
		this.funcOwner = funcOwner;
	}
}