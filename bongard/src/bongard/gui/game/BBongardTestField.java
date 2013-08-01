package bongard.gui.game;

import java.io.Serializable;
import java.util.HashMap;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BCheckBox;
import ollitos.gui.basic.BCheckBox.StateListener;
import ollitos.gui.basic.BDelayedSprite;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BZoomDrawable;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BBongardTestField extends BDrawableContainer implements IBSlidablePage, Serializable{

	public static enum State{
		undefined, solved,
	};
	
	public static HashMap<State, IBRasterProvider> _rasters;
	public static BSprite[] _sprites;
	public static HashMap<State, IBDrawable> _icons;
	
	static{
		_rasters = new HashMap<State, IBRasterProvider>();
		BRasterProviderCache rpc = BRasterProviderCache.instance();
		IBRasterProvider undefinedR = rpc.get(new BResourceLocator("/images/checkbox/undefined.png"), 110, 110 );
		_rasters.put( State.undefined, rpc.get(undefinedR, BPlatform.COLOR_WHITE, true ) );
		IBRasterProvider solvedR = rpc.get( new BResourceLocator("/images/checkbox/good.png"), 110, 110);
		_rasters.put( State.solved, rpc.get(solvedR, BPlatform.COLOR_WHITE, true ) );

		_sprites = new BSprite[State.values().length];
		for( State s: State.values() ){
			_sprites[s.ordinal()] = new BDelayedSprite(_rasters.get(s));
		}
		
		_icons = new HashMap<State, IBDrawable>();
		_icons.put( State.undefined, null );
		_icons.put( State.solved, new BBox( new BRectangle(0,0,15,15), BPlatform.COLOR_WHITE, true) );
	}
	
	
	private BResourceLocator _locator;
	transient private BProblem _problem;
	transient private BSprite _sprite;
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
		if( _problem == null ){
			_problem = new BProblem(_locator);
		}
		_problem.setUp();
        createProblemDrawables(_problem);
		
		_checkBox = createCheckBox();
		_checkBox.setState( state().ordinal() );
		addDrawable(_checkBox);
	}

    protected void createProblemDrawables(BProblem problem) {
        IBRasterProvider testImage = problem.testImage();
        //testImage = BRasterProviderCache.instance().get(testImage, new BRectangle(0,0,testImage.w(),testImage.h()), true);
        _sprite = new BDelayedSprite(testImage);
        _sprite.setAntialias(true);
        _sprite.transform().translate(originalSize().w() / 2, originalSize().h() / 2);
        _sprite.transform().rotate(Math.PI / 2);
        _sprite.transform().scale(.95, .95);
        BZoomDrawable z = new BZoomDrawable(_sprite);
        addDrawable(z);
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
		if( _sprite != null && _sprite.rasterProvider() != null ){
			_sprite.rasterProvider().dispose();
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
