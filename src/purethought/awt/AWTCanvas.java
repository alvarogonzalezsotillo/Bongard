package purethought.awt;

import java.awt.Component;

import purethought.gui.BCanvas;

public class AWTCanvas extends BCanvas{

	private Component _impl;
	
	/**
	 * 
	 * @param obj
	 */
	public AWTCanvas(Component impl) {
		_impl = impl;
	}
	
	public Component canvas(){
		return _impl;
	}
	
	@Override
	public void refresh() {
		canvas().repaint();
	}

}
