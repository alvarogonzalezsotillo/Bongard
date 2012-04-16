package ollitos.platform;

import ollitos.util.BException;


public interface IBSQLDatabase extends IBDisposable{
	public String name();
	public void execute(String sql, Object ... params) throws BException;
	public Object[][] query(String sql, Object ... params) throws BException;
}