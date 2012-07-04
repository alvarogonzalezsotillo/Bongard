package ollitos.platform;



public interface IBKeyValueDatabase extends IBDisposable{
	public String name();
	public boolean put( BState.Stateful s, Object ... key );
	public BState get( Object ... key );
}