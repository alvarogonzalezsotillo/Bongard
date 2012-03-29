package bongard.gui.game;

import bongard.gui.container.IBFlippableDrawable;
import bongard.gui.container.IBFlippableModel;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.problem.BProblem;

public class BBongardGameModel implements IBFlippableModel{

	private BResourceLocator[] _resources;
	private BBongardTestField[] _drawables;
	
	private BResourceLocator[] resources(){
		if (_resources == null) {
			_resources = BFactory.instance().cardExtractor().allProblems();
		}

		return _resources;
	}

	private BBongardTestField[] drawables(){
		if (_drawables == null) {
			_drawables = new BBongardTestField[resources().length];
			
		}

		return _drawables;
	}
	
	@Override
	public int width() {
		return resources().length;
	}

	@Override
	public IBFlippableDrawable drawable(int x) {
		BBongardTestField ret = drawables()[x];
		if( ret == null ){
			ret = new BBongardTestField(resources()[x]);
			drawables()[x] = ret;
		}
		return ret;
	}

	@Override
	public void dispose(int x) {
		if( drawables()[x] != null ){
			drawables()[x].dispose();
		}
	}

	@Override
	public BResourceLocator background() {
		return null;
	}
}
