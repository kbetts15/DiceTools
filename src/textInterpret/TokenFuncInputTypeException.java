package textInterpret;

public class TokenFuncInputTypeException extends RuntimeException
{
	TokenFuncInputTypeException()
	{
		super();
	}
	
	TokenFuncInputTypeException(String message)
	{
		super(message);
	}
	
	TokenFuncInputTypeException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	TokenFuncInputTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	TokenFuncInputTypeException(Throwable cause)
	{
		super(cause);
	}
}
