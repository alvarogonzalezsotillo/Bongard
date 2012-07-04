package ollitos.platform;


public interface IBKeyValueDatabase extends IBDisposable{
	
	public static class Util{
		
		public static String toString( Object o ){
			return String.valueOf(o);
		}
		public static String concatenate( Object ... key ){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < key.length; i++) {
				Object o = key[i];
				String s = toString(o);
				sb.append(s);
				if( i < key.length-1 ){
					sb.append('-');
				}
			}
			return sb.toString();
		}
	}
	
	public String name();
	public boolean put( Object table, BState.Stateful s, Object ... key );
	public BState get( Object table, Object ... key );
	public Object[] getKeys( Object table, Object ... subkey );
}