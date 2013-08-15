package ollitos.platform.andr;

import ollitos.platform.BGame;

public class AndrGame extends BGame{

	private AndrScreen _canvas;
	private AndrAnimator _animator;



	@Override
	public AndrScreen screen() {
		if( _canvas == null ){
			_canvas = new AndrScreen();
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
