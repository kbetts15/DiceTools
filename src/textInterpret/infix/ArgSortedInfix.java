package textInterpret.infix;

import diceTools.DicePoolMap;
import diceTools.ProbVector;
import textInterpret.TokenFuncInputTypeException;
import textInterpret.TokenInfix;

public abstract class ArgSortedInfix extends TokenInfix
{
	@Override
	public Object operate(Object objA, Object objB)
	{
		if (objA instanceof DicePoolMap)
		{
			DicePoolMap a = (DicePoolMap) objA;
			
			if (objB instanceof DicePoolMap) {
				DicePoolMap b = (DicePoolMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof ProbVector) {
				ProbVector b = (ProbVector) objB;
				return operateCase(a, b);}
			else if (objB instanceof Integer) {
				Integer b = (Integer) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof ProbVector)
		{
			ProbVector a = (ProbVector) objA;
			
			if (objB instanceof DicePoolMap) {
				DicePoolMap b = (DicePoolMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof ProbVector) {
				ProbVector b = (ProbVector) objB;
				return operateCase(a, b);}
			else if (objB instanceof Integer) {
				Integer b = (Integer) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof Integer)
		{
			Integer a = (Integer) objA;
			
			if (objB instanceof DicePoolMap) {
				DicePoolMap b = (DicePoolMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof ProbVector) {
				ProbVector b = (ProbVector) objB;
				return operateCase(a, b);}
			else if (objB instanceof Integer) {
				Integer b = (Integer) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else
			throw new TokenFuncInputTypeException();
	}
	
	public abstract Object operateCase(DicePoolMap a, DicePoolMap b);
	public abstract Object operateCase(DicePoolMap a, ProbVector b);
	public abstract Object operateCase(DicePoolMap a, Integer b);
	
	public abstract Object operateCase(ProbVector a, DicePoolMap b);
	public abstract Object operateCase(ProbVector a, ProbVector b);
	public abstract Object operateCase(ProbVector a, Integer b);
	
	public abstract Object operateCase(Integer a, DicePoolMap b);
	public abstract Object operateCase(Integer a, ProbVector b);
	public abstract Object operateCase(Integer a, Integer b);
}
