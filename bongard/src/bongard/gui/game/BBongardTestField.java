package bongard.gui.game;

import java.io.Serializable;
import java.util.HashMap;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BCheckBox;
import ollitos.gui.basic.BCheckBox.StateListener;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BZoomDrawable;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.raster.IBRasterProvider;
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BBongardTestField extends BDrawableContainer implements IBSlidablePage, Serializable{

	public static enum State{
		undefined, good, bad
	};
	
	public static HashMap<State, IBRasterProvider> _rasters;
	public static BSprite[] _sprites;
	public static HashMap<State, IBDrawable> _icons;
	
	static{
		BPlatform p = BPlatform.instance();
		
		_rasters = new HashMap<State, IBRasterProvider>();
		_rasters.put( State.undefined, p.raster( new BResourceLocator("/images/checkbox/undefined.png")) );
		_rasters.put( State.good, p.raster( new BResourceLocator("/images/checkbox/good.png")) );
		_rasters.put( State.bad, p.raster( new BResourceLocator("/images/checkbox/bad.png")) );
		
		_sprites = new BSprite[State.values().length];
		for( State s: State.values() ){
			_sprites[s.ordinal()] = p.sprite(_rasters.get(s));
		}
		
		_icons = new HashMap<State, IBDrawable>();
		_icons.put( State.undefined, null );
		_icons.put( State.good, new BBox( new BRectangle(0,0,15,15), BPlatform.COLOR_WHITE, true) );
		_icons.put( State.bad, new BBox( new BRectangle(0,0,15,15), BPlatform.COLOR_BLACK, true) );
	}
	
	
	private BResourceLocator _locator;
	transient private BProblem _problem;
	transient private BSprite _sprite;
	transient private IBDrawable _drawable;
	transient private BCheckBox _checkBox;
	private State _state = State.undefined;
	
	public State state(){
		return _state;
	}
	
	public BBongardTestField(BResourceLocator l){
		super( BGameField.computeOriginalSize() );
		setLocator(l);
	}


	private void setLocator(BResourceLocator l){
		_locator = l;
	}
	
	private BCheckBox createCheckBox(){
		BCheckBox c = new BCheckBox(_sprites);
		c.setSizeTo( new BRectangle(5, 5, 45, 45), false, true);
		c.addStateListener( new StateListener() {
			@Override
			public void stateChanged(BCheckBox b) {
				_state = State.values()[b.state()];
			}
		});
		return c;
	}

	
	/**
	 * 
	 * @param test
	 */
	private void setUpProblem(){
		BPlatform f = BPlatform.instance();
		if( _problem == null ){
			_problem = new BProblem(_locator);
		}
		_problem.setUp();
		_sprite = f.sprite(_problem.testImage());
		_sprite.setAntialias(true);
		_sprite.transform().translate( originalSize().w()/2, originalSize().h()/2 );
		_sprite.transform().rotate(Math.PI/2);
		_sprite.transform().scale(.95,.95);		
		BZoomDrawable z = new BZoomDrawable(_sprite);
		addDrawable(z);
		
		_checkBox = createCheckBox();
		_checkBox.setState( state().ordinal() );
		addDrawable(_checkBox);
	}

	@Override
	public IBRectangle originalSize() {
		return BGameField.computeOriginalSize();
	}

	@Override
	public IBDrawable icon() {
		return _icons.get( state() );
	}

	@Override
	public void setUp() {
		if( !disposed() ){
			return;
		}
		setUpProblem();
	}

	@Override
	public void dispose() {
		if( _problem != null ){
			_problem.dispose();
		}
		removeDrawables();
	}
	
	@Override
	public boolean disposed() {
		return _problem == null || _problem.disposed();
	}


	@Override
	public IBDrawable drawable() {
		return this;
	}

}
