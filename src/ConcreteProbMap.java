
public class ConcreteProbMap<K> extends ProbMap<K>
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean keyIsValid(Object key)
	{
		return true;
	}

	@Override
	public K sanitizeKey(K key)
	{
		return key;
	}

	@Override
	public ProbMap<K> get()
	{
		return new ConcreteProbMap<K>();
	}

}
