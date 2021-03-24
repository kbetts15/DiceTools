package diceTools;

public class TextInterpret
{
	public static DiceRollVector interpretDRV(String s)
	{
		return null; //TODO
	}
	
	public static ProbVector interpretPV(String s)
	{
		return null; //TODO
	}
	
	public static abstract class NestString
	{
		public abstract boolean isString();
		public abstract char bracketType();
	}
	
	public static final class NestStringOnly extends NestString
	{
		private final String str;
		private final char bracket;
		
		public NestStringOnly(String s, char bracket)
		{
			this.str = s;
			this.bracket = bracket;
		}
		
		@Override
		public boolean isString()
		{
			return true;
		}
		
		@Override
		public char bracketType()
		{
			return bracket;
		}
	}
	
	public static final class NestStringArr extends NestString
	{
		private final NestString[] arr;
		private final char bracket;
		
		public NestStringArr(String s, char bracket)
		{
			this.arr = makeNestStr(s);
			this.bracket = bracket;
		}
		
		private NestString[] makeNestStr(String s)
		{
			return null; //TODO
		}

		@Override
		public boolean isString()
		{
			return false;
		}

		@Override
		public char bracketType()
		{
			return bracket;
		}
	}
}
