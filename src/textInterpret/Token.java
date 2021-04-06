package textInterpret;

/**
 * Container class for various tokens used in expression parsing.
 * Accessor and mutator methods are provided to allow getting and setting
 * various variables related to different types of tokens.
 * <p><code>Token</code> provides no public constructor, and throws
 * {@link TokenTypeMismatchException} when getter or setter methods are called.
 * Instantiable subclasses override this behaviour when appropriate.
 * 
 * @see TokenType
 * 
 * @see VarToken
 * @see UnaryToken
 * @see InfixToken
 * @see FuncToken
 * @see BracketOpenToken
 * @see BracketCloseToken
 * @see CommaToken
 * @see EndToken
 * 
 * @author kieran
 */
public class Token
{
	/**
	 * Type of the <code>Token</code>.
	 * This indicates which methods are appropriate to call and which will throw exceptions.
	 * @see TokenType
	 */
	public final TokenType type;
	
	/**
	 * Name of the <code>Token</code>.
	 * This is used to print <code>Token</code>s as text
	 */
	private final String name;
	
	/**
	 * Instantiate a new {@link Token} with given type and name
	 * @param type	{@link Token#type}
	 * @param name	{@link Token#name}
	 */
	private Token(TokenType type, String name)
	{
		this.type = type;
		this.name = name;
	}
	
	@Override
	public final String toString()
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
	
	/**
	 * Return the variable stored by the <code>Token</code>
	 * 
	 * @return the stored variable
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#VAR}
	 * @see Token.VarToken
	 */
	public Object getVariable() {throw new TokenTypeMismatchException();}
	
	/**
	 * Return the priority of the <code>Token</code>
	 * 
	 * @return priority used in infix operation hierarchy
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_INFIX}
	 * @see {@link Token#setPriority}
	 * @see Token.InfixToken
	 */
	public int getPriority() {throw new TokenTypeMismatchException();}
	
	/**
	 * Set the priority of the <code>Token</code>
	 * 
	 * @param priority priority used in infix operation hierarchy
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_INFIX}
	 * @see {@link Token#getPriority}
	 * @see Token.InfixToken
	 */
	public void setPriority(int priority) {throw new TokenTypeMismatchException();}
	
	/**
	 * Get the unary function represented by the <code>Token</code>
	 * 
	 * @return the stored unary function
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_UNARY}
	 * @see Token.FuncToken
	 */
	public TokenUnary getFuncUnary() {throw new TokenTypeMismatchException();}
	
	/**
	 * Get the infix function represented by the <code>Token</code>
	 * 
	 * @return the stored infix function
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_INFIX}
	 * @see Token.InfixToken
	 */
	public TokenInfix getFuncInfix() {throw new TokenTypeMismatchException();}
	
	/**
	 * Get the argumented function represented by the <code>Token</code>
	 * 
	 * @return the stored argumented function
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_ARGS}
	 * @see Token.InfixToken
	 */
	public TokenFunc getFuncArgs() {throw new TokenTypeMismatchException();}
	
	/**
	 * Get the open bracket symbol represented by this <code>Token</code>
	 * if it is of type {@link TokenType#BRACKET_OPEN},
	 * or the matching open bracket symbol for the close bracket symbol represented
	 * by this <code>Token</code> if it is of type {@link TokenType#BRACKET_CLOSE}
	 * @return the open bracket character
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type
	 * {@link TokenType#BRACKET_OPEN} or {@link TokenType#BRACKET_CLOSE}
	 * @see Token.BracketOpenToken
	 * @see Token.BracketCloseToken
	 */
	public char getOpenSymbol() {throw new TokenTypeMismatchException();}
	
	/**
	 * Get the number of arguments used as input to the argumented function represented by this <code>Token</code>
	 * 
	 * @return number of arguments
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_ARGS}
	 * @see FuncToken
	 */
	public int getNumArgs() {throw new TokenTypeMismatchException();}
	
	/**
	 * Set the number of arguments used as input to the argumented function represented by this <code>Token</code>
	 * 
	 * @param numArgs number of arguments
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_ARGS}
	 * @see FuncToken
	 */
	public void setNumArgs(int numArgs) {throw new TokenTypeMismatchException();}
	
	/**
	 * Increment the number of arguments used as input to the argumented function represented by this <code>Token</code>
	 * 
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#FUNC_ARGS}
	 * @see FuncToken
	 */
	public void incNumArgs() {throw new TokenTypeMismatchException();}
	
	/**
	 * Get the <code>Token</code> representing an argumented function
	 * for which this open bracket is the beginning of an argument list.
	 * 
	 * 
	 * @return	the argumented function <code>Token</code> if it exists,
	 * 			<code>null</code> otherwise
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#BRACKET_OPEN}
	 * @see BracketOpenToken
	 */
	public Token getFuncOwner() {throw new TokenTypeMismatchException();}
	
	/**
	 * Set the <code>Token</code> representing an argumented function
	 * for which this open bracket is the beginning of an argument list.
	 * 
	 * @param funcOwner	the argumented function <code>Token</code>
	 * @throws TokenTypeMismatchException if the <code>Token</code> is not of type {@link TokenType#BRACKET_OPEN}
	 * @see BracketOpenToken
	 */
	public void setFuncOwner(Token funcOwner) {throw new TokenTypeMismatchException();}
	
	/**
	 * {@link Token} of type {@link TokenType#VAR}.
	 * Stores a variable which can be retrieved for evaluation
	 * 
	 * @author kieran
	 */
	public static final class VarToken extends Token
	{
		/**
		 * Stored variable
		 */
		private final Object var;
		
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#VAR}
		 * @param var	variable to store
		 */
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
	
	/**
	 * {@link Token} of type {@link TokenType#FUNC_UNARY}.
	 * Stores a unary function which can be retrieved for evaluation
	 * 
	 * @author kieran
	 */
	public static final class UnaryToken extends Token
	{
		/**
		 * Stored unary function
		 */
		private final TokenUnary unary;
		
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#FUNC_UNARY}
		 * @param unary Unary function to store
		 */
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
	
	/**
	 * {@link Token} of type {@link TokenType#FUNC_INFIX}.
	 * Stores an infix function and its priority.
	 * The priority is used when shunting to determine the
	 * order in which infix operators are applied.
	 * 
	 * @see {@link TextInterpret#shunt(java.util.List) TextInterpret.shunt}
	 * 
	 * @author kieran
	 */
	public static final class InfixToken extends Token
	{
		/**
		 * Stored infix function
		 */
		private final TokenInfix infix;
		
		/**
		 * Stored priority
		 */
		private final int priority;
		
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#FUNC_INFIX}
		 * 
		 * @param infix		Infix function to store
		 * @param priority	Priority of the infix function
		 */
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
	
	/**
	 * {@link Token} of type {@link TokenType#FUNC_ARGS}.
	 * Stores an argumented function and the number of arguments
	 * upon which it acts.
	 * 
	 * @author kieran
	 */
	public static final class FuncToken extends Token
	{
		/**
		 * Stored argumented function
		 */
		private final TokenFunc func;
		
		/**
		 * Stored number of arguments
		 */
		private int numArgs;
		
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#FUNC_ARGS}.
		 * @param func	Argumented function to store
		 */
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
	
	/**
	 * {@link Token} of type {@link TokenType#BRACKET_OPEN}.
	 * Stores the character representing the opening bracket
	 * and the <code>Token</code> for which this opening bracket
	 * begins an argument list, if any.
	 * 
	 * @author kieran
	 */
	public static final class BracketOpenToken extends Token
	{
		/**
		 * Character representing the open bracket
		 */
		private final char openSymbol;
		
		/**
		 * <code>Token</code> for which this opening bracket begins an argument list
		 */
		private Token funcOwner;
		
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#BRACKET_OPEN}
		 * @param openSymbol	character representing the open bracket
		 */
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
	
	/**
	 * {@link Token} of type {@link TokenType#BRACKET_CLOSE}.
	 * Stores the character representing the corresponding open bracket
	 * 
	 * @author kieran
	 */
	public static final class BracketCloseToken extends Token
	{
		/**
		 * Corresponding open bracket
		 */
		private final char openSymbol;
		
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#BRACKET_CLOSE}
		 * 
		 * @param openSymbol	Character representing the corresponding open bracket
		 * @param closeSymbol	Character representing the close bracket
		 */
		public BracketCloseToken(char openSymbol, char closeSymbol)
		{
			//TODO: is storing the openSymbol necessary? TextInterpret has a function to determine it from the close bracket
			super(TokenType.BRACKET_CLOSE, Character.toString(closeSymbol));
			this.openSymbol = openSymbol;
		}
		
		@Override
		public char getOpenSymbol()
		{
			return openSymbol;
		}
	}
	
	/**
	 * {@link Token} of type {@link TokenType#COMMA}.
	 * Used to separate arguments withing function argument lists
	 * 
	 * @author kieran
	 */
	public static final class CommaToken extends Token
	{
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#COMMA}
		 */
		public CommaToken()
		{
			super(TokenType.COMMA, ",");
		}
	}
	
	/**
	 * {@link Token} of type {@link TokenType#END}.
	 * Used to denote the end of an expression
	 * 
	 * @author kieran
	 */
	public static final class EndToken extends Token
	{
		/**
		 * Instantiates a {@link Token} of type {@link TokenType#END}
		 */
		public EndToken()
		{
			super(TokenType.END, "END");
		}
	}
	
}