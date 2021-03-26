package textInterpret;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TextInterpret
{
	private static final List<Map.Entry<String, TokenUnary>> unaryOperators;
	private static final List<Map.Entry<String, TokenInfix>> infixOperators;
	private static final List<Map.Entry<String, TokenFunc>> funcOperators;
	
	static
	{
		unaryOperators = new LinkedList<Map.Entry<String, TokenUnary>>();
		
		
		
		infixOperators = new LinkedList<Map.Entry<String, TokenInfix>>();
		
		
		
		funcOperators = new LinkedList<Map.Entry<String, TokenFunc>>();
		
		
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
}
