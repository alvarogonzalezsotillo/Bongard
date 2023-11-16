package ollitos.bot.map;

import java.util.HashMap;
import java.util.Map;

public abstract class BMap implements IBMap{
	private Map<String,BRoom> _idToRooms = new HashMap<String, BRoom>();
	
	@Override
	public BRoom room(String id) {
		BRoom ret = _idToRooms.get(id);
		if( ret == null ){
			ret = mapReader().readRoom(id);
			_idToRooms.put(id, ret);
		}
		return ret;
	}
}
