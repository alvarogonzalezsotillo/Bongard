package bongard.platform.andr;

import bongard.gui.game.BGame;

public class AndrGame extends BGame{

	private AndrCanvas _canvas;
	private AndrAnimator _animator;



	@Override
	public AndrCanvas canvas() {
		if( _canvas == null ){
			_canvas = new AndrCanvas();
		}
		return _canvas;
	}

	@Override
	public AndrAnimator animator() {
		if (_animator == null) {
			_animator = new AndrAnimator();
		}
		return _animator;
	}
	
}
