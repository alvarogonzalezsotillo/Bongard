package ollitos.platform.awt;

import ollitos.platform.state.IBKeyValueDatabase;
import ollitos.platform.state.IBKeyValueTable;

public class AWTKeyValueDatabase implements IBKeyValueDatabase{

	private String _name;


	public AWTKeyValueDatabase(String name){
		_name = name;
	}
	
	@Override
	public void setUp() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean disposed() {
		return false;
	}

	@Override
	public String name() {
		return _name;
	}

	
	@Override
	public IBKeyValueTable table(Object... name) {
		return new AWTKeyValueTable( IBKeyValueDatabase.Util.concatenate(name(), name), this ); 
	}

}
