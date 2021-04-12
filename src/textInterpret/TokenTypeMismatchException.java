package textInterpret;

@SuppressWarnings("serial")
public class TokenTypeMismatchException extends RuntimeException
{
	public TokenTypeMismatchException(String message)
	{
		super(message);
	}
	
	public TokenTypeMismatchException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public TokenTypeMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}