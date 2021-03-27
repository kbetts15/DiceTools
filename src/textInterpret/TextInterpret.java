package textInterpret;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import textInterpret.infix.AddInfix;
import textInterpret.infix.DivideInfix;
import textInterpret.infix.ModuloInfix;
import textInterpret.infix.MultiplyInfix;
import textInterpret.infix.PowerInfix;
import textInterpret.infix.SubtractInfix;

public class TextInterpret
{
	private static final List<PriorityEntry<? extends TokenUnary>> unaryOperators;
	private static final List<PriorityEntry<? extends TokenInfix>> infixOperators;
	private static final List<PriorityEntry<? extends TokenFunc>> funcOperators;
	
	static
	{
		unaryOperators = new LinkedList<PriorityEntry<? extends TokenUnary>>();
		
		
		
		infixOperators = new LinkedList<PriorityEntry<? extends TokenInfix>>();
		
		infixOperators.add(new PriorityEntry<TokenInfix>(new AddInfix(),		0));
		infixOperators.add(new PriorityEntry<TokenInfix>(new SubtractInfix(),	0));
		infixOperators.add(new PriorityEntry<TokenInfix>(new DivideInfix(),		1));
		infixOperators.add(new PriorityEntry<TokenInfix>(new MultiplyInfix(),	1));
		infixOperators.add(new PriorityEntry<TokenInfix>(new ModuloInfix(),		2));
		infixOperators.add(new PriorityEntry<TokenInfix>(new PowerInfix(),		3));
		
		funcOperators = new LinkedList<PriorityEntry<? extends TokenFunc>>();
		
		
	}
	
	public static Queue<String> group(String s)
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
	
	public static Queue<Token> tokenize(Queue<String> q)
	{
		Queue<Token> tQueue = new LinkedList<Token>();
		
		stringLoop: for (String s : q)
		{
			
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
			
			Token t = new Token(TokenType.VAR, s);
			tQueue.add(t);
		}
		
		Token end = new Token(TokenType.END, null);
		tQueue.add(end);
		
		return tQueue;
	}
	
	public static Queue<Token> shunt(Queue<Token> q)
	{
		Stack<Token> opStack = new Stack<Token>();
		Queue<Token> outQueue = new LinkedList<Token>();
		
		for (Token t : q)
		{
			if (t.type == TokenType.VAR)
				outQueue.add(t);
			else if (t.type == TokenType.FUNC_INFIX)
			{
				while (!opStack.isEmpty()
						&& opStack.peek().type != TokenType.BRACKET_OPEN
						&& opStack.peek().getPriority() >= t.getPriority())
					outQueue.add(opStack.pop());
				
				opStack.push(t);
			}
			else if (t.type == TokenType.END)
			{
				while (!opStack.isEmpty())
					outQueue.add(opStack.pop());
				
				outQueue.add(t);
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
			System.out.println("Token");
			System.out.printf("\tQueue: %s\n", queue.toString());
			System.out.printf("\tStack: %s\n", stack.toString());
			
			Token t = queue.poll();
			
			System.out.printf("\tGot: %s\n", t.toString());
			
			if (t.type == TokenType.VAR)
				stack.push(t);
			else if (t.type == TokenType.FUNC_INFIX)
			{
				//TODO: check for empty stack
				
				Token b = stack.pop();
				Token a = stack.pop();
				
				stack.push(t.getFuncInfix().apply(a, b));
			}
			
			System.out.printf("\tQueue: %s\n", queue.toString());
			System.out.printf("\tStack: %s\n", stack.toString());
		}
		
		//TODO: make sure there's only one Token left in the stack
		return stack.pop(); //TODO
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
