package purethought.gui;

import purethought.util.BFactory;

public abstract class BCanvas implements IBCanvas{
	private IBTransform _t = BFactory.instance().identityTransform();
	private IBCanvasDrawable _d;

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}

	@Override
	public void setDrawable(IBCanvasDrawable _d) {
		this._d = _d;
	}

	@Override
	public IBCanvasDrawable drawable() {
		return _d;
	}
	
}
