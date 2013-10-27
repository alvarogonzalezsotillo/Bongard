package ollitos.platform.raster;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.IBRaster;

public class BNewRasterProvider extends BRasterProvider{

	private IBRectangle _r;
	private static int _nextId = 0;
	private static Object _lock = new Object();
	private int _id;
	
	public BNewRasterProvider(int w, int h){
		_r = new BRectangle(0,0,w,h);
		_id = nextId();
	}

	private static int nextId() {
		synchronized (_lock) {
			int ret = _nextId;
			_nextId++;
			return ret;
		}
	}

	@Override
	public int w() {
		return (int) _r.w();
	}

	@Override
	public int h() {
		return (int) _r.h();
	}

	@Override
	public String key() {
		return getClass().getSimpleName() + _id;
	}

	@Override
	protected IBRaster createRaster() {
		return BPlatform.instance().rasterUtil().raster(_r);
	}

}
