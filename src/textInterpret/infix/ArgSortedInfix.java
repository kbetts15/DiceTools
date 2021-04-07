package textInterpret.infix;

import diceTools.DiceNumber;
import diceTools.DicePoolMap;
import diceTools.DiceRollMap;
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
			else if (objB instanceof DiceRollMap) {
				DiceRollMap b = (DiceRollMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof DiceNumber) {
				DiceNumber b = (DiceNumber) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof DiceRollMap)
		{
			DiceRollMap a = (DiceRollMap) objA;
			
			if (objB instanceof DicePoolMap) {
				DicePoolMap b = (DicePoolMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof DiceRollMap) {
				DiceRollMap b = (DiceRollMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof DiceNumber) {
				DiceNumber b = (DiceNumber) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else if (objA instanceof DiceNumber)
		{
			DiceNumber a = (DiceNumber) objA;
			
			if (objB instanceof DicePoolMap) {
				DicePoolMap b = (DicePoolMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof DiceRollMap) {
				DiceRollMap b = (DiceRollMap) objB;
				return operateCase(a, b);}
			else if (objB instanceof DiceNumber) {
				DiceNumber b = (DiceNumber) objB;
				return operateCase(a, b);}
			else
				throw new TokenFuncInputTypeException();
		}
		else
			throw new TokenFuncInputTypeException();
	}
	
	public abstract Object operateCase(DicePoolMap a, DicePoolMap b);
	public abstract Object operateCase(DicePoolMap a, DiceRollMap b);
	public abstract Object operateCase(DicePoolMap a, DiceNumber b);
	
	public abstract Object operateCase(DiceRollMap a, DicePoolMap b);
	public abstract Object operateCase(DiceRollMap a, DiceRollMap b);
	public abstract Object operateCase(DiceRollMap a, DiceNumber b);
	
	public abstract Object operateCase(DiceNumber a, DicePoolMap b);
	public abstract Object operateCase(DiceNumber a, DiceRollMap b);
	public abstract Object operateCase(DiceNumber a, DiceNumber b);
}
