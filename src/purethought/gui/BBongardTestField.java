package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class BBongardTestField extends BTopDrawable{

	private BProblem _problem;
	private BSprite _sprite;
	
	/**
	 * 
	 * @param test
	 */
	public void setProblem( BProblemLocator test ){
		BFactory f = BFactory.instance();
		_problem = BCardExtractor.extract(test);
		
		_sprite = f.sprite(_problem.testImage());
		_sprite.translate( originalSize().w()/2, originalSize().h()/2 );
	}

	@Override
	public IBRectangle originalSize() {
		return _sprite.raster().originalSize();
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_sprite.draw(c, t);
	}

}
