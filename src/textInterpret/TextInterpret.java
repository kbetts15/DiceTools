package textInterpret;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TextInterpret
{
	//TODO: implement using shunting-yard algorithm (https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
	
	private static final List<Map.Entry<String, TokenUnary>> unaryOperators;
	private static final List<Map.Entry<String, TokenInfix>> infixOperators;
	private static final List<Map.Entry<String, TokenFunc>> funcOperators;
	
	static
	{
		unaryOperators = new LinkedList<Map.Entry<String, TokenUnary>>();
		infixOperators = new LinkedList<Map.Entry<String, TokenInfix>>();
		funcOperators = new LinkedList<Map.Entry<String, TokenFunc>>();  
	}
	
	private static enum TokenState
	{
		READY,
		NUMBER,
		STRING
	}
	
	public static enum TokenType
	{
		VAR,
		FUNC_UNARY,
		FUNC_INFIX,
		FUNC_ARGS,
		BRACKET_OPEN,
		BRACKET_CLOSE
	}
	
	public static LinkedList<String> group(String s)
	{
		LinkedList<String> tokenList = new LinkedList<String>();
		TokenState state = TokenState.READY;
		
		StringIterable sIter = new StringIterable(s);
		StringBuilder sBuild = new StringBuilder();
		
		for (char c : sIter)
		{
			if (isSpace(c))
			{
				if (sBuild.length() > 0)
				{
					tokenList.add(sBuild.toString());
					sBuild = new StringBuilder();
				}
				
				continue;
			}
			
			boolean goToNextChar = false;
			
			while (!goToNextChar)
			{
				goToNextChar = true;
				
				switch (state)
				{
					case READY:
						
						if (isNumeric(c))
							state = TokenState.NUMBER;
						else if (isStringy(c))
							state = TokenState.STRING;
						else
						{
							tokenList.add(Character.toString(c));
							break;
						}
						
						sBuild.append(c);
						break;
						
					case NUMBER:
						
						if (isNumeric(c))
							sBuild.append(c);
						else
						{
							tokenList.add(sBuild.toString());
							sBuild = new StringBuilder();
							
							goToNextChar = false;
							state = TokenState.READY;
						}
						
						break;
					
					case STRING:
						
						if (isStringy(c))
							sBuild.append(c);
						else
						{
							tokenList.add(sBuild.toString());
							sBuild = new StringBuilder();
							
							goToNextChar = false;
							state = TokenState.READY;
						}
						
						break;
					
					default:
						
						goToNextChar = false;
						state = TokenState.READY;
				}
			}
		}
		
		if (sBuild.length() > 0)
			tokenList.add(sBuild.toString());
		
		return tokenList;
	}
	
	public static List<Token> tokenize(List<String> li)
	{
		return null;
	}
	
	private static boolean isNumeric(char c)
	{
		return (c >= '0' && c <= '9') || c == '.';
	}
	
	private static boolean isStringy(char c)
	{
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}
	
	private static boolean isSpace(char c)
	{
		return c == ' ' || c == '\t';
	}
	
	public static final class Token
	{
		public final TokenType type;
		
		//VAR
		private Object var = null;
		
		//FUNC_UNARY
		private TokenUnary funcUnary = null;
		
		//FUNC_INFIX
		private TokenInfix funcInfix = null;
		
		//FUNC_ARGS
		private TokenFunc funcArgs = null;
		private int numArgs = 0;
		
		//BRACKET_xxxx
		private char openSymbol = '\0';
		
		//BRACKET_OPEN
		private Token funcOwner = null;
		
		public Token(TokenType type)
		{
			this.type = type;
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

		public char getOpenSymbol()
		{
			if (type != TokenType.BRACKET_OPEN && type != TokenType.BRACKET_CLOSE)
				throw new TokenTypeMismatchException();
			
			return openSymbol;
		}

		public void setOpenSymbol(char openSymbol)
		{
			if (type != TokenType.BRACKET_OPEN && type != TokenType.BRACKET_CLOSE)
				throw new TokenTypeMismatchException();
			
			this.openSymbol = openSymbol;
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

		@SuppressWarnings("serial")
		public static class TokenTypeMismatchException extends RuntimeException
		{
			TokenTypeMismatchException()
			{
				super();
			}
			
			TokenTypeMismatchException(String message)
			{
				super(message);
			}
			
			TokenTypeMismatchException(String message, Throwable cause)
			{
				super(message, cause);
			}
			
			TokenTypeMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
			{
				super(message, cause, enableSuppression, writableStackTrace);
			}
			
			TokenTypeMismatchException(Throwable cause)
			{
				super(cause);
			}
		}
	}
	
	public static abstract class TokenUnary implements UnaryOperator<Token>
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
	
	public static abstract class TokenInfix implements BinaryOperator<Token>
	{
		@Override
		public final Token apply(Token a, Token b)
		{
			Object varA = a.getVariable();
			Object varB = b.getVariable();
			
			Object varNew = operate(varA, varB);
			
			Token newToken = new Token(TokenType.VAR);
			newToken.setVariable(varNew);
			return newToken;
		}
		
		public abstract Object operate(Object objA, Object objB);
	}
	
	public static abstract class TokenFunc implements Function<List<Token>, Token>
	{
		@Override
		public final Token apply(List<Token> li)
		{
			List<Object> objList = new LinkedList<Object>();
			for (Token t : li)
				objList.add(t.getVariable());
			
			Object variable = operate(objList);
			
			Token newToken = new Token(TokenType.VAR);
			newToken.setVariable(variable);
			return newToken;
		}
		
		public abstract Object operate(List<? extends Object> li);
	}
	
	public static class StringIterable implements Iterable<Character>
	{
		private final String s;
		
		public StringIterable(String s)
		{
			this.s = s;
		}

		@Override
		public Iterator<Character> iterator()
		{
			return new StringIterator();
		}
		
		private class StringIterator implements Iterator<Character>
		{
			private int pos = 0;
			
			@Override
			public boolean hasNext()
			{
				return pos < s.length();
			}

			@Override
			public Character next()
			{
				if (!hasNext())
					throw new NoSuchElementException();
				
				Character c = s.charAt(pos);
				pos++;
				return c;
			}
		}
	}
}
