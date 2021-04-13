package diceTools;

/**
 * <code>Number</code> which can store either an <code>int</code> or <code>double</code>,
 * and which is aware of the data representation of the number it stores.
 * 
 * @author kieran
 */
@SuppressWarnings("serial")
public abstract class DiceNumber extends Number implements Comparable<Number>
{
	/**
	 * Private constructor prevents external subclasses
	 */
	private DiceNumber() {}
	
	/**
	 * Create a <code>DiceNumber<code> which is a copy of a given <code>DiceNumber</code>
	 * 
	 * @param n		<code>DiceNumber</code> to copy
	 * @return		copy of the given <code>DiceNumber</code>
	 */
	public static DiceNumber copy(DiceNumber n)
	{
		if (n.isInt())
			return new DiceInteger(n.intValue());
		else
			return new DiceDouble(n.doubleValue());
	}
	
	/**
	 * Returns whether the number stored by this <code>DiceNumber</code> i
	 * is stored internally as an <code>int</code> or a <code>double</code>
	 * 
	 * @return	true iff the internal representation of the number is an <code>int</code>,
	 * 			false iff the internal representation of the number is a <code>double</code>
	 */
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
		
		if (isInt())
			return intValue() == dn.intValue();
		else
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
	
	@Override
	public int hashCode()
	{
		return Double.hashCode(doubleValue());
	}
	
	/**
	 * {@link DiceNumber} which stores its number internally as an <code>int</code>
	 * 
	 * @author kieran
	 */
	public static final class DiceInteger extends DiceNumber
	{
		/**
		 * Number stored as an <code>int</code>
		 */
		public final int value;
		
		/**
		 * Constructs a <code>DiceInteger</code> to store a given <code>int<code>
		 * @param value		<code>int<code> to store
		 */
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
	
	/**
	 * {@link DiceNumber} which stores its number internally as a <code>double</code>
	 * 
	 * @author kieran
	 */
	public static final class DiceDouble extends DiceNumber
	{
		/**
		 * Number stored as a <code>double</code>
		 */
		public final double value;
		
		/**
		 * Constructs a <code>DiceDouble</code> to store a given <code>double<code>
		 * @param value		<code>double<code> to store
		 */
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
