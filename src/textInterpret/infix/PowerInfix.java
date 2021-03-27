package textInterpret.infix;

public class PowerInfix extends NumericInfix
{

	@Override
	public Integer operateCase(Integer a, Integer b)
	{
		return (int) Math.pow(a, b);
	}

	@Override
	public String getName()
	{
		return "^";
	}

}
