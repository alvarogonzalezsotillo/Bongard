package ollitos.bot.physics.behaviour;

import static ollitos.bot.geom.BDirection.east;
import static ollitos.bot.geom.BDirection.north;
import static ollitos.bot.geom.BDirection.south;
import static ollitos.bot.geom.BDirection.west;

import java.util.ArrayList;
import java.util.HashMap;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.util.BException;

public class BDefaultSpriteBehaviour extends BSpriteBehaviour implements IBPhysicalListener{
	
	private static final String ISO_ITEMS  = "/iso/items";
	private BItemType _t;
	private int _index;
	private RasterGroup _rasterGroup;

	public BDefaultSpriteBehaviour(BItemType t){
		_t = t;
		_rasterGroup = rasterGroup(_t);
	}
	
	@SuppressWarnings("serial")
	private static class RasterGroup extends HashMap<BDirection, IBRaster[]>{
		public int countFrames(){
			return get(south).length;
		}
		
		public void check(){
			int s = get(south).length;
			int n = get(north).length;
			int w = get(west).length;
			int e = get(east).length;

			// ONLY SOUTH
			if( n == 0 && w == 0 && e == 0 && s != 0 ){
				put( north, get( south) );
				put( west, get( south) );
				put( east, get( south) );
				return;
			}

			// ONLY SOUTH AND EAST
			if( n == 0 && w == 0 && e != 0 && e == s ){
				put( north, get( south) );
				put( west, get( east) );
				return;
			}

			if( s != n || s != w || s != e ){
				throw new BException("Bad frames", null );
			}
		}
	};

	
	private static HashMap<BItemType,RasterGroup> _rasterGroups = new HashMap<BItemType, BDefaultSpriteBehaviour.RasterGroup>();
	
	private static RasterGroup rasterGroup(BItemType t){
		RasterGroup ret = _rasterGroups.get(t);
		if( ret != null ){
			return ret;
		}

		ret = createRasterGroup(t);
		
		_rasterGroups.put(t, ret);
		return ret;
	}

	private static RasterGroup createRasterGroup(BItemType t) {
		RasterGroup ret;
		ret = new RasterGroup();
		String rootPath = ISO_ITEMS + "/" + t.rasterGroup();
		ArrayList<IBRaster> rasters = new ArrayList<IBRaster>();
		for( BDirection d: BDirection.values() ){
			rasters.clear();
			while(true){
				int c = rasters.size();
				String path = rootPath + "/" + d.name() + "-" + c + ".png";
				BResourceLocator l = new BResourceLocator(path);
				IBRaster raster = BRasterProviderCache.instance().get(l,1,1).raster();
				if( raster == null ){
					break;
				}
				rasters.add(raster);
			}
			IBRaster[] rasterArray = (IBRaster[]) rasters.toArray(new IBRaster[rasters.size()]);
			ret.put(d, rasterArray);
		}
		ret.check();
		return ret;
	}

	@Override
	public IBRaster currentFrame(BDirection d) {
		return _rasterGroup.get(d)[_index];
	}

	@Override
	public void advanceFrame(){
		_index++;
		_index%=_rasterGroup.countFrames();
	}

	@Override
	public IBRaster[] frames(BDirection d) {
		return _rasterGroup.get(d);
	}

	public void dump(){
		BPlatform.instance().logger().log(this,_rasterGroup);
	}
	
	public static void main(String[] args) {
		new BDefaultSpriteBehaviour(BItemType.bot).dump();
	}

	@Override
	public void itemAdded(IBPhysicalItem i) {
	}

	@Override
	public void itemRemoved(IBPhysicalItem i) {
	}

	@Override
	public void collision(IBCollision collision) {
	}

	@Override
	public void stepFinished() {
		advanceFrame();
	}

	@Override
	public void itemMoved(IBPhysicalItem i, IBRegion oldRegion) {
	}
}
