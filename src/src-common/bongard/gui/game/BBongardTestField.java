package bongard.gui.game;

import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBCanvas;
import bongard.gui.basic.IBRectangularDrawable;
import bongard.gui.container.BDrawableContainer;
import bongard.gui.container.BFlippableContainer;
import bongard.gui.container.IBFlippableDrawable;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.problem.BCardExtractor;
import bongard.problem.BProblem;

public class BBongardTestField extends BDrawableContainer implements IBFlippableDrawable{

	private BProblem _problem;
	private BSprite _sprite;
	private BFlippableContainer _container;
	
	
	public BBongardTestField(BResourceLocator l){
		this(new BProblem(l));
	}

	public BBongardTestField(BProblem l){
		setProblem(l);
	}
	
	/**
	 * 
	 * @param test
	 */
	public void setProblem( BProblem problem ){
		BFactory f = BFactory.instance();
		_problem = problem;
		
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

	@Override
	public BFlippableContainer flippableContainer() {
		return _container;
	}


	@Override
	public void setFlippableContainer(BFlippableContainer c) {
		_container = c;
		
	}

	@Override
	public IBRectangularDrawable icon() {
		return null;
	}

}
