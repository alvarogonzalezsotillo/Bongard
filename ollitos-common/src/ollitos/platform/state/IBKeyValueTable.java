package ollitos.platform.state;

public interface IBKeyValueTable {
	String name();
	IBKeyValueDatabase database();
	
	public boolean putBytes( byte[] b, Object ... key );
	public byte[] getBytes( Object ... key );
}
