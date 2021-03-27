package textInterpret;

@SuppressWarnings("serial")
public class TokenFuncInputTypeException extends RuntimeException
{
	public TokenFuncInputTypeException()
	{
		super();
	}
	
	public TokenFuncInputTypeException(String message)
	{
		super(message);
	}
	
	public TokenFuncInputTypeException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public TokenFuncInputTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public TokenFuncInputTypeException(Throwable cause)
	{
		super(cause);
	}
}
