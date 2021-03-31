package textInterpret;

public class Token
{
	public final TokenType type;
	private final String name;
	
	private Token(TokenType type, String name)
	{
		this.type = type;
		this.name = name;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder(type == TokenType.END ? "END" : name);
		sb.append('<');
		
		switch (type)
		{
			case VAR:
				sb.append('V');
				break;
			case BRACKET_OPEN:
				sb.append('B');
				break;
			case BRACKET_CLOSE:
				sb.append('b');
				break;
			case COMMA:
				sb.append('C');
			case FUNC_UNARY:
				sb.append('U');
				break;
			case FUNC_INFIX:
				sb.append('I');
				sb.append(getPriority());
				break;
			case FUNC_ARGS:
				sb.append('A');
				sb.append(getNumArgs());
				break;
			case END:
				sb.append('E');
				break;
		}
		
		sb.append('>');
		return sb.toString();
	}
	
	//These functions are overwritten in subclasses
	public Object getVariable()					{throw new TokenTypeMismatchException();}
	public int getPriority()					{throw new TokenTypeMismatchException();}
	public void setPriority(int priority)		{throw new TokenTypeMismatchException();}
	public TokenUnary getFuncUnary()			{throw new TokenTypeMismatchException();}
	public TokenInfix getFuncInfix()			{throw new TokenTypeMismatchException();}
	public TokenFunc getFuncArgs()				{throw new TokenTypeMismatchException();}
	public char getOpenSymbol() 				{throw new TokenTypeMismatchException();}
	public int getNumArgs() 					{throw new TokenTypeMismatchException();}
	public void setNumArgs(int numArgs)			{throw new TokenTypeMismatchException();}
	public void incNumArgs()					{throw new TokenTypeMismatchException();}
	public Token getFuncOwner()					{throw new TokenTypeMismatchException();}
	public void setFuncOwner(Token funcOwner)	{throw new TokenTypeMismatchException();}
	
	public static final class VarToken extends Token
	{
		private final Object var;
		
		public VarToken(Object var)
		{
			super(TokenType.VAR, var.toString());
			this.var = var;
		}
		
		@Override
		public Object getVariable()
		{
			return var;
		}
	}
	
	public static final class UnaryToken extends Token
	{
		private final TokenUnary unary;
		
		public UnaryToken(TokenUnary unary)
		{
			super(TokenType.FUNC_UNARY, unary.getName());
			this.unary = unary;
		}
		
		@Override
		public TokenUnary getFuncUnary()
		{
			return unary;
		}
	}
	
	public static final class InfixToken extends Token
	{
		private final TokenInfix infix;
		private final int priority;
		
		public InfixToken(TokenInfix infix, int priority)
		{
			super(TokenType.FUNC_INFIX, infix.getName());
			this.infix = infix;
			this.priority = priority;
		}
		
		@Override
		public int getPriority()
		{
			return priority;
		}
		
		@Override
		public TokenInfix getFuncInfix()
		{
			return infix;
		}
	}
	
	public static final class FuncToken extends Token
	{
		private final TokenFunc func;
		private int numArgs;
		
		public FuncToken(TokenFunc func)
		{
			super(TokenType.FUNC_ARGS, func.getName());
			this.func = func;
		}
		
		@Override
		public TokenFunc getFuncArgs()
		{
			return func;
		}
		
		@Override
		public int getNumArgs()
		{
			return numArgs;
		}

		@Override
		public void setNumArgs(int numArgs)
		{
			this.numArgs = numArgs;
		}
	}
	
	public static final class BracketOpenToken extends Token
	{
		private final char openSymbol;
		private Token funcOwner;
		
		public BracketOpenToken(char openSymbol)
		{
			super(TokenType.BRACKET_OPEN, Character.toString(openSymbol));
			this.openSymbol = openSymbol;
		}

		@Override
		public char getOpenSymbol()
		{
			return openSymbol;
		}

		@Override
		public Token getFuncOwner()
		{
			return funcOwner;
		}

		@Override
		public void setFuncOwner(Token funcOwner)
		{
			this.funcOwner = funcOwner;
		}
	}
	
	public static final class BracketCloseToken extends Token
	{
		private final char openSymbol;
		
		public BracketCloseToken(char openSymbol, char closeSymbol)
		{
			super(TokenType.BRACKET_CLOSE, Character.toString(closeSymbol));
			this.openSymbol = openSymbol;
		}
		
		@Override
		public char getOpenSymbol()
		{
			return openSymbol;
		}
	}
	
	public static final class CommaToken extends Token
	{
		public CommaToken()
		{
			super(TokenType.COMMA, ",");
		}
	}
	
	public static final class EndToken extends Token
	{
		public EndToken()
		{
			super(TokenType.END, "END");
		}
	}
	
//	END
}