package ollitos.platform.awt;

import java.util.prefs.Preferences;

import ollitos.platform.state.IBKeyValueDatabase;
import ollitos.platform.state.IBKeyValueTable;

public class AWTKeyValueTable implements IBKeyValueTable {

	private String _name;
	private AWTKeyValueDatabase _db;

	public AWTKeyValueTable(String name, AWTKeyValueDatabase db){
		_name = name;
		_db = db;
	}
	
	@Override
	public String name() {
		return _name;
	}

	@Override
	public IBKeyValueDatabase database() {
		return _db;
	}

	private Preferences preferences() {
		Preferences ur = Preferences.userRoot();
		ur = ur.node(this.getClass().getName());
		return ur;
	}

	
	@Override
	public boolean putBytes(byte[] b, Object... key) {
		String k = IBKeyValueDatabase.Util.concatenate(key);
		if( b == null ){
			preferences().remove(k);
		}
		else{
			preferences().putByteArray(k, b );
		}
		return true;
	}

	@Override
	public byte[] getBytes(Object... key) {
		String k = IBKeyValueDatabase.Util.concatenate(key);
		byte[] b = preferences().getByteArray(k,null);
		return b;
	}
}
