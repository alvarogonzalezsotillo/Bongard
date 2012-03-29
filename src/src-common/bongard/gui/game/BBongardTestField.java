package bongard.gui.game;

import java.io.Serializable;

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
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BBongardTestField extends BDrawableContainer implements IBFlippableDrawable, Serializable{

	private static final boolean FOLLOW_SPRITE_SIZE = false;
	private BResourceLocator _locator;
	transient private BProblem _problem;
	transient private BSprite _sprite;
	transient private BFlippableContainer _container;
	
	
	public BBongardTestField(BResourceLocator l){
		setLocator(l);
	}


	private void setLocator(BResourceLocator l){
		_locator = l;
	}	
	/**
	 * 
	 * @param test
	 */
	private void setUpProblem(){
		BFactory f = BFactory.instance();
		_problem = new BProblem(_locator);
		_sprite = f.sprite(_problem.testImage());
		_sprite.setAntialias(true);
		_sprite.transform().translate( originalSize().w()/2, originalSize().h()/2 );
		_sprite.transform().rotate(Math.PI/2);
	}

	@Override
	public IBRectangle originalSize() {
		if( FOLLOW_SPRITE_SIZE){
			return _sprite.raster().originalSize();
		}
		else{
			return BGameField.computeOriginalSize();
		}
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		BFactory.instance().logger().log(this, t.toString() );
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

	@Override
	public void setUp() {
		setUpProblem();
	}

	@Override
	public void dispose() {
		_problem.dispose();
	}
	
	@Override
	public boolean disposed() {
		return _problem.disposed();
	}

}
