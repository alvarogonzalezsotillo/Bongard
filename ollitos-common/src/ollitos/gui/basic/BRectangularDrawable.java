package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;
import ollitos.util.BTransformUtil;

public abstract class BRectangularDrawable extends BDrawable{
	
	private IBRectangle _originalSize;

	public BRectangularDrawable() {
		this( new BRectangle(0, 0, 1, 1));
	}
	
	public BRectangularDrawable(IBRectangle r) {
		setOriginalSize(r);
	}

	public void setOriginalSize(IBRectangle r) {
		_originalSize = r;
	}

	public void setSizeTo( IBRectangle r, boolean keepAspectRatio, boolean fitInside){
		BTransformUtil.setTo(transform(), originalSize(), r, keepAspectRatio, fitInside);
	}
	
	@Override
	public final boolean inside(IBPoint p, IBTransform aditionalTransform) {
		IBTransform t = transform();
		if( aditionalTransform != null ){
			IBTransform tt = BPlatform.instance().identityTransform();
			tt.concatenate(t);
			tt.concatenate(aditionalTransform);
			t = tt;
		}
		
		IBTransform inverseT = t.inverse();
		
		IBPoint inverseP = inverseT.transform(p);		
		
		return IBRectangle.Util.inside(originalSize(), inverseP);
	}

	public IBRectangle originalSize(){
		return _originalSize;
	}


}
