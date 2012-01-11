package purethought.gui;

import purethought.util.BFactory;

public abstract class BCanvas implements IBCanvas{
	private IBTransform _t = BFactory.instance().identityTransform();

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}
	
}
