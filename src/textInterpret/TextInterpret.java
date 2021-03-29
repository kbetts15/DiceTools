package textInterpret;

import java.util.Deque;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import diceTools.ImmutableList;
import textInterpret.infix.AddInfix;
import textInterpret.infix.CombineInfix;
import textInterpret.infix.DicePoolInfix;
import textInterpret.infix.DiceRollInfix;
import textInterpret.infix.DivideInfix;
import textInterpret.infix.ModuloInfix;
import textInterpret.infix.MultiplyInfix;
import textInterpret.infix.PowerInfix;
import textInterpret.infix.SubtractInfix;
import textInterpret.unary.NegativeUnary;

public class TextInterpret
{
	private static final List<TokenUnary> unaryOperators;
	private static final List<PriorityEntry<? extends TokenInfix>> infixOperators;
	private static final List<TokenFunc> funcOperators; //TODO: This doesn't need to be priority based
	
	private static final EnumMap<TokenType, ImmutableList<TokenType>> tokenTypeOrder;
	
	static
	{
		unaryOperators = new LinkedList<TokenUnary>();
		
		unaryOperators.add(new NegativeUnary());
		
		infixOperators = new LinkedList<PriorityEntry<? extends TokenInfix>>();
		
		infixOperators.add(new PriorityEntry<TokenInfix>(new AddInfix(),		0));
		infixOperators.add(new PriorityEntry<TokenInfix>(new SubtractInfix(),	0));
		infixOperators.add(new PriorityEntry<TokenInfix>(new DivideInfix(),		1));
		infixOperators.add(new PriorityEntry<TokenInfix>(new MultiplyInfix(),	1));
		infixOperators.add(new PriorityEntry<TokenInfix>(new ModuloInfix(),		2));
		infixOperators.add(new PriorityEntry<TokenInfix>(new PowerInfix(),		3));
		infixOperators.add(new PriorityEntry<TokenInfix>(new CombineInfix(),	4));
		infixOperators.add(new PriorityEntry<TokenInfix>(new DiceRollInfix(),	5));
		infixOperators.add(new PriorityEntry<TokenInfix>(new DicePoolInfix(),	5));
		
		funcOperators = new LinkedList<TokenFunc>();
		
		tokenTypeOrder = new EnumMap<TokenType, ImmutableList<TokenType>>(TokenType.class);
		
		tokenTypeOrder.put(TokenType.VAR,			new ImmutableList<TokenType>(new TokenType[]{TokenType.BRACKET_CLOSE, TokenType.COMMA, TokenType.FUNC_INFIX, TokenType.END}));
		tokenTypeOrder.put(TokenType.FUNC_UNARY,	new ImmutableList<TokenType>(new TokenType[]{TokenType.VAR, TokenType.FUNC_UNARY, TokenType.FUNC_ARGS, TokenType.BRACKET_OPEN}));
		tokenTypeOrder.put(TokenType.FUNC_INFIX,	new ImmutableList<TokenType>(new TokenType[]{TokenType.VAR, TokenType.FUNC_UNARY, TokenType.FUNC_ARGS, TokenType.BRACKET_OPEN}));
		tokenTypeOrder.put(TokenType.FUNC_ARGS,		new ImmutableList<TokenType>(new TokenType[]{TokenType.BRACKET_OPEN}));
		tokenTypeOrder.put(TokenType.BRACKET_OPEN,	new ImmutableList<TokenType>(new TokenType[]{TokenType.VAR, TokenType.FUNC_UNARY, TokenType.FUNC_ARGS, TokenType.BRACKET_OPEN, TokenType.BRACKET_CLOSE}));
		tokenTypeOrder.put(TokenType.BRACKET_CLOSE,	new ImmutableList<TokenType>(new TokenType[]{TokenType.FUNC_INFIX, TokenType.BRACKET_CLOSE, TokenType.COMMA, TokenType.END}));
		tokenTypeOrder.put(TokenType.COMMA,			new ImmutableList<TokenType>(new TokenType[]{TokenType.VAR}));
		tokenTypeOrder.put(TokenType.END,			new ImmutableList<TokenType>(new TokenType[]{}));
	}
	
	public static Queue<String> group(String s) //TODO: return a list
	{
		Queue<String> tokenQueue = new LinkedList<String>();
		TokenState state = TokenState.READY;
		
		StringIterable sIter = new StringIterable(s);
		StringBuilder sBuild = new StringBuilder();
		
		for (char c : sIter)
		{
			if (isSpace(c))
			{
				if (sBuild.length() > 0)
				{
					tokenQueue.add(sBuild.toString());
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
							tokenQueue.add(Character.toString(c));
							break;
						}
						
						sBuild.append(c);
						break;
						
					case NUMBER:
						
						if (isNumeric(c))
							sBuild.append(c);
						else
						{
							tokenQueue.add(sBuild.toString());
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
							tokenQueue.add(sBuild.toString());
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
			tokenQueue.add(sBuild.toString());
		
		return tokenQueue;
	}
	
	public static Queue<Token> tokenize(Queue<String> q) //TODO: return a list
	{
		Deque<Token> tQueue = new LinkedList<Token>();
		
		stringLoop: for (String s : q)
		{
			
			if (s.length() == 1)
			{
				char c = s.charAt(0);
				
				Token t;
				
				boolean foundChar = true;
				
				if (c == ',')
					t = new Token(TokenType.COMMA, ",");
				else if (isOpenBracket(c))
				{
					t = new Token(TokenType.BRACKET_OPEN, Character.toString(c));
					t.setOpenSymbol(c);
				}
				else
				{
					char openBracket = getOpenBracket(c);
					if (openBracket == '\0')
					{
						t = null;
						foundChar = false;
					}
					else
					{
						t = new Token(TokenType.BRACKET_CLOSE, Character.toString(c));
						t.setOpenSymbol(openBracket);
					}
				}
				
				if (foundChar)
				{
					tQueue.add(t);
					continue;
				}
			}
			
			if (isNumeric(s.charAt(0)))
			{
				Token t = new Token(TokenType.VAR, s);
				
				//TODO: handle cases with multiple decimal points
				
				if (!s.contains("."))
					t.setVariable(Integer.parseInt(s));
				else
					t.setVariable(Double.parseDouble(s));
				
				tQueue.add(t);
				
				continue stringLoop;
			}
			
			TokenType lastType = tQueue.peekLast() == null
					? TokenType.BRACKET_OPEN
					: tQueue.peekLast().type;
			
			if (tokenTypeOrder.get(lastType).contains(TokenType.FUNC_UNARY))
				for (TokenUnary tu : unaryOperators)
				{
					if (!tu.getName().equals(s))
						continue;
					
					Token t = new Token(TokenType.FUNC_UNARY, tu.getName());
					t.setFuncUnary(tu);
					
					tQueue.add(t);
					continue stringLoop;
				}
			
			for (PriorityEntry<? extends TokenInfix> pe : infixOperators)
			{
				TokenInfix ti = pe.getElement();
				int prio = pe.getPriority();
				
				if (!ti.getName().equals(s))
					continue;
				
				Token t = new Token(TokenType.FUNC_INFIX, ti.getName());
				t.setFuncInfix(ti);
				t.setPriority(prio);
				
				tQueue.add(t);
				continue stringLoop;
			}
			
			for (TokenFunc tf : funcOperators)
			{
				if (!tf.getName().equals(s))
					continue;
				
				Token t = new Token(TokenType.FUNC_ARGS, tf.getName());
				t.setFuncArgs(tf);
				
				tQueue.add(t);
				continue stringLoop;
			}
			
			Token t = new Token(TokenType.VAR, s);
			t.setVariable(s); //TODO: Make a variable lookup list
			
			tQueue.add(t);
		}
			
		Token end = new Token(TokenType.END, null);
		tQueue.add(end);
		
		return tQueue;
	}
	
	public static void validateTokenQueue(Queue<Token> q) //TODO: call this in tokenize or shunt?
	{
		Iterator<Token> iter = q.iterator();
		
		/* First check that each token is makes sense based on the previous token
		 * (eg throw an error for "(x3)") */
		
		if (!iter.hasNext())
			return;
		
		Token lastToken = iter.next();
		
		if (lastToken.type != TokenType.BRACKET_OPEN
				&& lastToken.type != TokenType.FUNC_UNARY
				&& lastToken.type != TokenType.FUNC_ARGS
				&& lastToken.type != TokenType.VAR)
			throw new RuntimeException(String.format("First token cannot be of type %s", lastToken.type.toString()));
		
		while (iter.hasNext())
		{
			Token t = iter.next();
			
			if (!tokenTypeOrder.get(lastToken.type).contains(t.type))
				throw new RuntimeException(String.format("Tokens of type %s cannot be followed by tokens of type %s",
						lastToken.type, t.type));
			
			lastToken = t;
		}
		
		/* Check that brackets are balanced and that commas only appear inside brackets TODO */
	}
	
	public static Queue<Token> shunt(Queue<Token> q)
	{
		Queue<Token> inQueue = new LinkedList<Token>(q);
		Queue<Token> outQueue = new LinkedList<Token>();
		
		Stack<Token> opStack = new Stack<Token>();
		Stack<Integer> bracketArgs = new Stack<Integer>();
		
		while (!inQueue.isEmpty())
		{
			Token t = inQueue.poll();
			
			switch (t.type)
			{
				case VAR:
					
					outQueue.add(t);
					break;
					
				case BRACKET_OPEN:
					
					bracketArgs.push(t.getFuncOwner() == null ? -1 : 1);
					//FALL THROUGH
					
				case FUNC_UNARY:
					
					opStack.push(t);
					break;
					
				case FUNC_INFIX:
					
					while (!opStack.isEmpty())
					{
						Token opNext = opStack.peek();
						
						if (opNext.type == TokenType.FUNC_UNARY
								|| (opNext.type == TokenType.FUNC_INFIX
										&& opNext.getPriority() >= t.getPriority()))
						{
							outQueue.add(opStack.pop());
							continue;
						}
						
						break;
					}
					
					opStack.push(t);
					break;
				
				case FUNC_ARGS:
					
					if (inQueue.isEmpty())
						throw new RuntimeException("Functions cannot be the last Token");
					
					Token nextToken = inQueue.peek();
					
					if (nextToken.type != TokenType.BRACKET_OPEN
							|| nextToken.getOpenSymbol() != '(')
						throw new RuntimeException("Functions must be followed by '('");
					
					nextToken.setFuncOwner(t);
					
					opStack.push(t);
					break;
					
				case BRACKET_CLOSE:
					
					boolean foundBracket = false;
					
					while (!opStack.isEmpty())
					{
						Token topStack = opStack.peek();
						
						if (topStack.type != TokenType.BRACKET_OPEN)
						{
							outQueue.add(opStack.pop());
							continue;
						}
						
						foundBracket = true;
						
						if (topStack.getOpenSymbol() != t.getOpenSymbol())
							throw new RuntimeException(String.format("Brackets do not match: %c, %c",
									topStack.getOpenSymbol(), t.getOpenSymbol()));
						
						int numArgs = bracketArgs.pop();
						
						if (topStack.getFuncOwner() == null)
						{
							if(numArgs > 1)
								throw new RuntimeException(String.format(
										"Only one argument is permitted for non-function bracket. %d provided",
										numArgs));
						}
						else
							topStack.getFuncOwner().setNumArgs(numArgs);
						
						opStack.pop();
						break;
					}
					
					if (!foundBracket)
						throw new RuntimeException("Failed to find opening bracket");
					
					break;
					
				case END:
					
					while (!opStack.isEmpty())
						outQueue.add(opStack.pop());
					
					outQueue.add(t);
					break;
					
				case COMMA:
					
					if (bracketArgs.isEmpty())
						throw new RuntimeException("Using ',' outside of brackets is not allowed");
					
					int numArgs = bracketArgs.pop();
					numArgs++;
					bracketArgs.push(numArgs);
					
					break;
					
				default:
					
					throw new RuntimeException(String.format("Shunting of %s Tokens is not yet implemented", t.type));
			}
		}
		
		return outQueue;
	}
	
	public static Object evaluate(Queue<Token> q)
	{
		Queue<Token> queue = new LinkedList<Token>(q);
		Stack<Token> stack = new Stack<Token>();
		
		while (!queue.isEmpty())
		{
			Token t = queue.poll();
			
			switch (t.type)
			{
				case VAR:
					
					stack.push(t);
					break;
					
				case FUNC_UNARY:
					
					Token operand = stack.pop();
					stack.push(t.getFuncUnary().apply(operand));
					break;
				
				case FUNC_INFIX:
					
					//TODO: check for empty stack
					
					Token b = stack.pop();
					Token a = stack.pop();
					
					stack.push(t.getFuncInfix().apply(a, b));
					break;
					
				case END:
					
					//TODO: does this make sense? Is the END token necessary?
					return stack.pop().getVariable();
					
				default:
					
					throw new RuntimeException(String.format("Evaluation of %s Tokens is not yet implemented", t.type));

			}
		}
		
		//TODO: make sure there's only one Token left in the stack
		return stack.pop().getVariable(); //TODO
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
	
	private static boolean isOpenBracket(char c)
	{
		switch (c)
		{
			case '(':
//			case '<':
//			case '{':
				return true;
			default:
				return false;
		}
	}
	
	private static char getOpenBracket(char c)
	{
		switch (c)
		{
			case ')': return '(';
//			case '>': return '<';
//			case '}': return '{';
			default: return '\0';
		}
	}
	
	public static class PriorityEntry<T>
	{
		private final T t;
		private final int priority;
		
		public PriorityEntry(T t, int priority)
		{
			this.t = t;
			this.priority = priority;
		}
		
		public T getElement()
		{
			return t;
		}
		
		public int getPriority()
		{
			return priority;
		}
	}
}
