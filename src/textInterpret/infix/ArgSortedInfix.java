package textInterpret.infix;

import diceTools.DiceRollVector;
import diceTools.ProbVector;
import textInterpret.TokenFuncInputTypeException;
import textInterpret.TokenInfix;

public abstract class ArgSortedInfix extends TokenInfix
{
	@Override
	public Object operate(Object objA, Object objB)
	{
		if (objA instanceof DiceRollVector)
		{
			DiceRollVector a = (DiceRollVector) objA;
			
			if (objB instanceof DiceRollVector) {
				DiceRollVector b = (DiceRollVector) objB;
				return operate(a, b);}
			else if (objB instanceof ProbVector) {
				ProbVector b = (ProbVector) objB;
				return operate(a, b);}
			else if (objB instanceof Integer) {
				Integer b = (Integer) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof ProbVector)
		{
			ProbVector a = (ProbVector) objA;
			
			if (objB instanceof DiceRollVector) {
				DiceRollVector b = (DiceRollVector) objB;
				return operate(a, b);}
			else if (objB instanceof ProbVector) {
				ProbVector b = (ProbVector) objB;
				return operate(a, b);}
			else if (objB instanceof Integer) {
				Integer b = (Integer) objB;
				return operate(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof Integer)
		{
			Integer a = (Integer) objA;
			
			if (objB instanceof DiceRollVector) {
				DiceRollVector b = (DiceRollVector) objB;
				return operate(a, b);}
			else if (objB instanceof ProbVector) {
				ProbVector b = (ProbVector) objB;
				return operate(a, b);}
			else if (objB instanceof Integer) {
				Integer b = (Integer) objB;
				return operate(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else
			throw new TokenFuncInputTypeException();
	}
	
	public abstract Object operateCase(DiceRollVector a, DiceRollVector b);
	public abstract Object operateCase(DiceRollVector a, ProbVector b);
	public abstract Object operateCase(DiceRollVector a, Integer b);
	
	public abstract Object operateCase(ProbVector a, DiceRollVector b);
	public abstract Object operateCase(ProbVector a, ProbVector b);
	public abstract Object operateCase(ProbVector a, Integer b);
	
	public abstract Object operateCase(Integer a, DiceRollVector b);
	public abstract Object operateCase(Integer a, ProbVector b);
	public abstract Object operateCase(Integer a, Integer b);
}
