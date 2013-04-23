package ollitos.bot.map.bsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import bsh.EvalError;
import bsh.Interpreter;
import ollitos.bot.map.BMap;
import ollitos.bot.map.BRoom;
import ollitos.bot.map.BRoomBuilder;
import ollitos.bot.map.IBMap;
import ollitos.bot.map.IBMapReader;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.util.BException;

public class BBeanShellMapReader implements IBMapReader{
	private BResourceLocator _root;
	private BeanShellMap _map;
	
	class BeanShellMap extends BMap{

		@Override
		public BRoom initialRoom() {
			return room( "InitRoom");
		}

		@Override
		public BRoom room(String id) {
			return readRoom(id);
		}

		@Override
		public IBMapReader mapReader() {
			return BBeanShellMapReader.this;
		}
				
	}
	
	public BBeanShellMapReader(String root){
		this( new BResourceLocator(root) );
	}
	
	public BBeanShellMapReader(BResourceLocator root){
		_root = root;
	}

	@Override
	public IBMap readMap() {
		if( _map == null ){
			_map = new BeanShellMap();
		}
		return _map;
	}

	@Override
	public BRoom readRoom(String id) {
		BResourceLocator l = computeLocator(id);
		BRoom room = new BRoom(readMap());
		try {
			populateRoom(l, room);
		}
		catch (BException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BException( e.toString(), e );
		}
		return room;
	}

	private BResourceLocator computeLocator(String id){
		return BResourceLocator.combine(_root, id + ".bsh" );
	}
	
	private Interpreter _interpreter;
	private Interpreter interpreter() throws EvalError{
		if (_interpreter == null) {
			_interpreter = new Interpreter();
		}

		return _interpreter;
	}
	
	private void setCommonVars(Interpreter i, BRoom room) throws EvalError{
		i.getNameSpace().clear();
		BRoomBuilder builder = new BRoomBuilder(room);
		i.set( "room", builder.room() );
		i.set( "builder", builder );
		i.eval( "void addLayers(String[][] s){ builder.addLayers(s); }" );
		i.eval( "void setDoorDestination(String doorIndex, String destRoomId, String destDoorIndex){ room.setDoorDestination(doorIndex,destRoomId,destDoorIndex); }" );
	}
	
	public void populateRoom(BResourceLocator script, BRoom room) throws EvalError, IOException{
		Interpreter i = interpreter();
		setCommonVars(i, room);
		Reader r = reader(script);
		i.eval(r);
	}

	private Reader reader(BResourceLocator script) throws IOException {
		InputStream in = BPlatform.instance().open( script );
		return new InputStreamReader(in);
	}	
}
