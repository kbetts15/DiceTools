package textInterpret.infix;

public class DivideInfix extends NumericInfix
{

	@Override
	public Integer operateCase(Integer a, Integer b)
	{
		return a / b;
	}

	@Override
	public String getName()
	{
		return "/";
	}

}
