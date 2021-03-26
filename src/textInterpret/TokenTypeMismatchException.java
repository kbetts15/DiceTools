package textInterpret;

@SuppressWarnings("serial")
public class TokenTypeMismatchException extends RuntimeException
{
	TokenTypeMismatchException()
	{
		super();
	}
	
	TokenTypeMismatchException(String message)
	{
		super(message);
	}
	
	TokenTypeMismatchException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	TokenTypeMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	TokenTypeMismatchException(Throwable cause)
	{
		super(cause);
	}
}