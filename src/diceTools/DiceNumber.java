package diceTools;

@SuppressWarnings("serial")
public abstract class DiceNumber extends Number implements Comparable<Number>
{
	private DiceNumber() {}
	
	public static DiceNumber copy(DiceNumber n)
	{
		if (n.isInt())
			return new DiceInteger(n.intValue());
		else
			return new DiceDouble(n.doubleValue());
	}
	
	public abstract boolean isInt();
	
	@Override
	public int compareTo(Number otherNum)
	{
		return Double.compare(this.doubleValue(), otherNum.doubleValue());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DiceNumber))
			return false;
		
		DiceNumber dn = (DiceNumber) obj;
		
		if (isInt() != dn.isInt())
			return false;
		
		return doubleValue() == dn.doubleValue();
	}
	
	@Override
	public String toString()
	{
		if (isInt())
			return Integer.toString(intValue());
		else
			return String.format("%.3f", doubleValue());
	}
	
	public static final class DiceInteger extends DiceNumber
	{
		public final int value;
		
		public DiceInteger(int value)
		{
			this.value = value;
		}

		@Override public boolean isInt()		{return true;}
		@Override public double doubleValue()	{return value;}
		@Override public float floatValue()		{return value;}
		@Override public int intValue()			{return value;}
		@Override public long longValue()		{return value;}
	}
	
	public static final class DiceDouble extends DiceNumber
	{
		public final double value;
		
		public DiceDouble(double value)
		{
			this.value = value;
		}
		
		@Override public boolean isInt()		{return false;}
		@Override public double doubleValue()	{return value;}
		@Override public float floatValue()		{return (float) value;}
		@Override public int intValue()			{return (int) value;}
		@Override public long longValue()		{return (long) value;}
	}
}
