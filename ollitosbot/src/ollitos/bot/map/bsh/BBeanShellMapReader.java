package ollitos.bot.map.bsh;

import ollitos.bot.map.BMap;
import ollitos.bot.map.BRoom;
import ollitos.bot.map.IBMap;
import ollitos.bot.map.IBMapReader;
import ollitos.platform.BResourceLocator;

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
		BBeanShellRoomReader r = new BBeanShellRoomReader(_map, l);
		return r.readRoom();
		
	}

	private BResourceLocator computeLocator(String id){
		return BResourceLocator.combine(_root, id + ".bsh" );
	}
}
